package com.example.myapplication

fun getBiljke(): MutableList<Biljka> {
    return mutableListOf(
        Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
                    medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE,
            MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
        jela = listOf("Salata od paradajza", "Punjene tikvice"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
    ),
    Biljka(
        naziv = "Nana (Mentha spicata)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO,
        MedicinskaKorist.PROTIVBOLOVA),
    profilOkusa = ProfilOkusaBiljke.MENTA,
    jela = listOf("Jogurt sa voćem", "Gulaš"),
    klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
    zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
    ),
    Biljka(
        naziv = "Kamilica (Matricaria chamomilla)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE,
            MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Čaj od kamilice"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Ružmarin (Rosmarinus officinalis)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO,
        MedicinskaKorist.REGULACIJAPRITISKA),
    profilOkusa = ProfilOkusaBiljke.AROMATICNO,
    jela = listOf("Pečeno pile", "Grah","Gulaš"),
    klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
    zemljisniTipovi = listOf(Zemljiste.SLJUNOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Lavanda (Lavandula angustifolia)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE,
        MedicinskaKorist.PODRSKAIMUNITETU),
    profilOkusa = ProfilOkusaBiljke.AROMATICNO,
    jela = listOf("Jogurt sa voćem"),
    klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
    zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
    ),
        Biljka(
            naziv = "Maslačak (Taraxacum officinale)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Osobe alergične na biljke iz porodice Asteraceae mogu imati alergijske reakcije na maslačak.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
        profilOkusa = ProfilOkusaBiljke.GORKO,
        jela = listOf("Salata od mladih listova maslačka", "Čaj od korijena maslačka"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.PJESKOVITO)
    ),
        Biljka(
            naziv = "Kopriva (Urtica dioica)",
            porodica = "Urticaceae (koprive)",
            medicinskoUpozorenje = "Pri rukovanju s koprivom treba biti oprezan zbog mogućnosti iritacije kože.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.GORKO,
            jela = listOf("Juha od koprive", "Pesto od koprive"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.PLANINSKA),
            zemljisniTipovi = listOf(Zemljiste.SLJUNOVITO, Zemljiste.CRNICA)
        ),
        Biljka(
            naziv = "Majčina dušica (Thymus vulgaris)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Prekomjerna konzumacija može izazvati iritaciju gastrointestinalnog trakta.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Piletina s majčinom dušicom", "Umak od rajčice s majčinom dušicom"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.GLINENO)
        ),
        Biljka(
            naziv = "Brusnica (Vaccinium oxycoccos)",
            porodica = "Ericaceae (vresovke)",
            medicinskoUpozorenje = "Osobe koje uzimaju antikoagulanse trebaju izbjegavati konzumaciju brusnice zbog povećanog rizika od krvarenja.",
            medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPRITISKA, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.LJUTO,
            jela = listOf("Sok od brusnice", "Muffini s brusnicom"),
            klimatskiTipovi = listOf(KlimatskiTip.PLANINSKA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.ILOVACA, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Zova (Sambucus nigra)",
            porodica = "Adoxaceae (zovke)",
            medicinskoUpozorenje = "Plodovi i sjemenke zove mogu biti toksični ako se konzumiraju u velikim količinama.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("Sok od zove", "Pita od zove"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SREDOZEMNA),
            zemljisniTipovi = listOf(Zemljiste.CRNICA, Zemljiste.ILOVACA)
        )
    )
}