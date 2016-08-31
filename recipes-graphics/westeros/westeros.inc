LICENSE = "Apache-2.0"

PV = "1.0+gitr${SRCPV}"

SRC_URI = "${WESTEROS_URI}"
SRCREV = "${WESTEROS_SRCREV}"
WESTEROS_URI ?= "git://github.com/kuscsik/westeros;branch=drm_support"
WESTEROS_SRCREV ?= "03cbbd9e5af6d4c4c2a6a42ac4a841debd42bef7"
LICENSE_LOCATION ?= "${S}/LICENSE"
LIC_FILES_CHKSUM = "file://${LICENSE_LOCATION};md5=703f52ef306fb8ef480744cc0da110fe"
