SUMMARY = "Tvheadend TV streaming server"
HOMEPAGE = "https://www.lonelycoder.com/redmine/projects/tvheadend"

DEPENDS = "avahi zlib openssl python-native libav"

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=9cae5acac2e9ee2fc3aec01ac88ce5db"

SRC_URI = "git://github.com/tvheadend/tvheadend.git;name=tvh \
           git://linuxtv.org/dtv-scan-tables.git;name=dvb;destsuffix=git/data/dvb-scan \
           file://0001-Move-tvheadend-specific-LD-CFLAGS-into-a-helper-vari.patch \
           file://tvheadend.init \
"
SRCREV_tvh = "a95ef0b237a01e6fe6a29c0cd76f98a3a216c1ce"
SRCREV_dvb = "82115b4a1f1039d5a2f93f2b7ddc4b2d2713b7ae"
SRCREV_FORMAT = "tvh"

PV = "3.9+git${SRCPV}"

S = "${WORKDIR}/git"

do_configure() {
    # The fetcher ensures the mux list is up to date
    sed -i -e 's:exit 1:exit 0:g' ${S}/support/getmuxlist

    ./configure --prefix=${prefix} \
                --libdir=${libdir} \
                --bindir=${bindir} \
                --datadir=${datadir} \
                --arch=${TARGET_ARCH} \
                --disable-bundle
}

do_install() {
    oe_runmake install DESTDIR=${D}
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/tvheadend.init  ${D}${sysconfdir}/init.d/tvheadend
}

FILES_${PN} += "${datadir}/${BPN} /etc/init.d/tvheadend"

inherit update-rc.d

INITSCRIPT_NAME = "tvheadend"
INITSCRIPT_PARAMS = "defaults 10"
