require linux-hikey.inc

DESCRIPTION = "Hisilicon 4.5 Kernel"

PV = "4.5+git"

SRCREV_kernel="623d1d7bbd697e5793a135d5334e02c0e9889ba1"
SRCREV_linuxoptee = "5fcce5d5800a60957141f1d963edfd199480bfcb"


SRC_URI = "git://github.com/kuscsik/linux;branch=hikey-4.5-drm;protocol=https;name=kernel \
        ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'git://github.com/OP-TEE/optee_linuxdriver;protocol=https;branch=master;destsuffix=${S}/optee_linuxdriver;name=linuxoptee', '', d)} \
           file://defconfig;name=defconfig  \
          "
SRCREV_FORMAT = "kernel"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "hikey"
KERNEL_IMAGETYPE ?= "Image"
KERNEL_DEVICETREE_hikey = "hisilicon/hi6220-hikey.dtb"

do_compile_append() {
    if [ -n "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee', '', d)}" ]; then
        oe_runmake O=${B} M=${S}/optee_linuxdriver -C ${B} ${PARALLEL_MAKE} modules CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS}
    fi
}

kernel_do_install_append() {
    if [ -n "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee', '', d)}" ]; then
        oe_runmake M=${S}/optee_linuxdriver INSTALL_MOD_PATH="${D}" modules_install
    fi
}
