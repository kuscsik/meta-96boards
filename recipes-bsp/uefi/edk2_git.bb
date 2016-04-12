SUMMARY = "Modern, feature-rich, cross-platform firmware development environment for the UEFI and PI specifications"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://BaseTools/License.txt;md5=a041d47c90fd51b4514d09a5127210e6 \
                   "

DEPENDS += "util-linux-native"

inherit deploy

PV = "2.0+${SRCPV}"

SRCREV_FORMAT = "edk2-atf"

EDKBRANCH ?= "hikey"

SRCREV_edk2_hikey = "7da6cdf18127ee52e79f3cf990c8d630fd037784"

ATFBRANCH ?= "hikey_gendrv"

SRCREV_atf_hikey = "9d3b0ecf9ffaf52f918b3870d69a55c42fb7fbac"

SRCREV_uefitools = "fdb4ea64f3f7a228d70e3b3646fb52943d703823"

LINAROPKGBRANCH ?= "hikey"

SRCREV_linaropkg = "3e28745338147c4e2a4751b7a1196fcdcf0b7695"

SRCREV_opteeos = "${OPTEE_OS_REVISON}"

OPTEEOSBRANCH ?= "master"
SRC_URI = "git://github.com/96boards/edk2.git;name=edk2;branch=${EDKBRANCH} \
           git://github.com/linaro-swg/arm-trusted-firmware.git;name=atf;branch=${ATFBRANCH};destsuffix=git/atf \
           git://git.linaro.org/uefi/uefi-tools.git;name=uefitools;destsuffix=git/uefi-tools \ 
           git://github.com/96boards/LinaroPkg.git;name=linaropkg;destsuffix=git/LinaroPkg;branch=${LINAROPKGBRANCH} \
           git://github.com/OP-TEE/optee_os.git;name=opteeos;destsuffix=git/optee-os;branch=${OPTEEOSBRANCH}  \
           file://0001-build-atf-in-64bit-mode.patch; \
          "


S = "${WORKDIR}/git"

export AARCH64_TOOLCHAIN = "GCC49"
export EDK2_DIR = "${S}"
export UEFI_TOOLS_DIR = "${S}/uefi-tools"
export CROSS_COMPILE_64 = "${TARGET_PREFIX}"
export CROSS_COMPILE_32 = "${TARGET_PREFIX}"

# Workaround a gcc 4.9 feature
# https://lists.96boards.org/pipermail/dev/2015-March/000146.html 
CFLAGS = " -fno-delete-null-pointer-checks"
BUILD_CFLAGS += "-Wno-error=unused-result"

# This is a bootloader, so unset OE LDFLAGS.
# OE assumes ld==gcc and passes -Wl,foo
LDFLAGS = ""

export UEFIMACHINE ?= "${MACHINE_ARCH}"

OPTEE_BUILD_ARGS = "${@base_contains('MACHINE_FEATURES', 'optee', '-s optee-os', '', d)}"

do_compile() {

    # We can't pass the sysroot to OP TEE build system in 
    # OE way. Without setting the sysroot, the OP TEE build
    # scripts fail to locate the gcc static libraries.
    export CFLAGS="$CFLAGS --sysroot=${STAGING_DIR_HOST} "

    ${UEFI_TOOLS_DIR}/uefi-build.sh -b RELEASE -a ${S}/atf -c LinaroPkg/platforms.config ${OPTEE_BUILD_ARGS} ${UEFIMACHINE} 
}

do_install() {
    install -d ${D}/boot
    install -m 0644 ${S}/atf/build/${UEFIMACHINE}/release/*.bin ${D}/boot/
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${S}/atf/build/${UEFIMACHINE}/release/*.bin ${DEPLOYDIR}/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES_${PN} += "/boot"

addtask deploy before do_build after do_compile

