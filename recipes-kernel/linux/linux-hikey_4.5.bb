require linux-hikey.inc

DESCRIPTION = "Hisilicon 4.5 Kernel"

PV = "4.5+git"

SRCREV_kernel="341ffa21df0d657426775b303985b1a11da66488"


SRC_URI = "git://github.com/kuscsik/linux;branch=hikey_optee_smaf;protocol=https;name=kernel \
           file://defconfig;name=defconfig  \
          "
SRCREV_FORMAT = "kernel"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "hikey"
KERNEL_IMAGETYPE ?= "Image"
KERNEL_DEVICETREE_hikey = "hisilicon/hi6220-hikey.dtb"

