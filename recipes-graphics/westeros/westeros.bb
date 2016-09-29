include westeros.inc

SUMMARY = "Westeros compositor for wayland"

PACKAGECONFIG ??= "incapp inctest increndergl incsbprotocol xdgv4"

PACKAGECONFIG[incapp] = "--enable-app=yes"
PACKAGECONFIG[inctest] = "--enable-test=yes"
PACKAGECONFIG[inctest] = "--enable-test=yes"
PACKAGECONFIG[increndergl] = "--enable-rendergl=yes"
PACKAGECONFIG[incsbprotocol] = "--enable-sbprotocol=yes"
PACKAGECONFIG[xdgv4] = "--enable-xdgv4=yes"
PACKAGECONFIG[xdgv5] = "--enable-xdgv5=yes"
PACKAGECONFIG[xdgv5] = "--enable-=yes"

COMPATIBLE_MACHINE = "(hikey-32|raspberrypi2|dragonboard-410c-32)"

WESTEROS_GRAPHIC_PLATFORM_hikey-32 = "drm"
WESTEROS_GRAPHIC_PLATFORM_dragonboard-410c-32 = "drm"
WESTEROS_GRAPHIC_PLATFORM_raspberrypi2 = "rpi"
WESTEROS_GRAPHIC_PLATFORM_brcm = "brcm"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--with-platform='${WESTEROS_GRAPHIC_PLATFORM}'"

DEPENDS = "wayland libdrm libxkbcommon"

RDEPENDS_${PN} = "xkeyboard-config"

inherit autotools pkgconfig

SECURITY_CFLAGS_remove = "-fpie"
SECURITY_CFLAGS_remove = "-pie"

TARGET_CFLAGS+= "\
    -I${S}/simplebuffer \
    -I${S}/simplebuffer/protocol \
    -I${S}/simpleshell/ \
    -I${S}/simpleshell/protocol\
    "

do_compile_prepend() {
   export SCANNER_TOOL=${STAGING_BINDIR_NATIVE}/wayland-scanner
   oe_runmake -C ${S}/simplebuffer/protocol;
   oe_runmake -C ${S}/simpleshell/protocol;
   oe_runmake -C ${S}/protocol
}
