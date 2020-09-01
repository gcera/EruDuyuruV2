package gcera.app.eruduyuru.objects

import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.models.RequestData
import gcera.app.eruduyuru.models.UserPref

object AppData {
    fun getRequestData(index: Int, requestForMuh: Boolean = false): RequestData {
        return if (requestForMuh) {
            RequestData(
                url = MUH_LINKLER[index],
                select0 = MUH_SELECT_0,
                select1 = MUH_SELECT_1,
                indexSelect = MUH_INDEX_SELECT,
                departName = MUH_BOLUMLER[index],
                imgId = MUH_LOGOLAR[index]
            )
        } else {
            RequestData(
                url = FAKULTE_LINKLER[index],
                select0 = FAKULTELER_SELECT_0[index],
                select1 = FAKULTELER_SELECT_1[index],
                indexSelect = FAKULTELER_INDEX_SELECT[index],
                departName = FAKULTELER[index],
                imgId = FAKULTE_LOGOLAR[index]
            )
        }
    }

//#ctl01 > div.container.containerShadow > div:nth-child(2) > div > div > div:nth-child(2) > div:nth-child(2) > div > div.DetayCenter.col-lg-9.col-md-9.col-sm-11.col-xs-11 > div.row.Detay > div > div > div > div.DetayText > div > div > div:nth-child(1)

    fun getMealData() = RequestData(
        url = "https://www.erciyes.edu.tr/kategori/KAMPUSTE-YASAM/Yemek-Hizmetleri/22/167",
        select0 = "#ctl01 > div.container.containerShadow > div:nth-child(2) > div > div > div:nth-child(2) > div:nth-child(2) > div > div.DetayCenter.col-lg-9.col-md-9.col-sm-11.col-xs-11 > div.row.Detay > div > div > div > div.DetayText > div > div > div:nth-child(1)",
        select1 = "",
        indexSelect = "",
        departName = "",
        imgId = 0
    )

    fun getEruAnnounceData() = RequestData(
        url = "https://www.erciyes.edu.tr/Tum-Duyurular/0",
        select0 = "#ctl01 > div.container.containerShadow > div:nth-child(2) > div > div > div:nth-child(2) > div:nth-child(2) > div > div.DetayCenter.col-lg-9.col-md-9.col-sm-11.col-xs-11 > div > div > div > div > div.DetayText > div > div.row.DuyuruListe > div:nth-child(",
        select1 = ") > a",
        indexSelect = "#ctl01 > div.container.containerShadow > div:nth-child(2) > div > div > div:nth-child(2) > div:nth-child(2) > div > div.DetayCenter.col-lg-9.col-md-9.col-sm-11.col-xs-11 > div > div > div > div > div.DetayText > div.DuyuruDetay",
        imgId = R.drawable.ic_main_announcement,
        departName = "Erciyes Üni. Duyurular"
    )

    fun getEruNewsData() = RequestData(
        url = "https://www.erciyes.edu.tr/Tum-Duyurular/-3",
        select0 = "#ctl01 > div.container.containerShadow > div:nth-child(2) > div > div > div:nth-child(2) > div:nth-child(2) > div > div.DetayCenter.col-lg-9.col-md-9.col-sm-11.col-xs-11 > div > div > div > div > div.DetayText > div > div.row.DuyuruListe > div:nth-child(",
        select1 = ") > a",
        indexSelect = "#ctl01 > div.container.containerShadow > div:nth-child(2) > div > div > div:nth-child(2) > div:nth-child(2) > div > div.DetayCenter.col-lg-9.col-md-9.col-sm-11.col-xs-11 > div > div > div > div > div.DetayText > div.DuyuruDetay",
        imgId = R.drawable.ic_news,
        departName = "Erciyes Üni. Haberler"
    )

    fun getObisisData()=RequestData(
        url = "https://obisis.erciyes.edu.tr/",
        select0 = "#ctl03_dlDuyuru > tbody > tr:nth-child(",
        select1 = ") > td > span.NormalBlue",
        indexSelect = "#ctl03_dlDuyuru > tbody > tr:nth-child(",
        imgId = R.drawable.ic_obisis,
        departName = "Obisis Duyurular"
    )

    const val obisisIndexSelect_=") > td > span.Normal"

    val defaultUserPref = UserPref(
        isDarkThemeActive = false,
        isAnnounceActive = true,
        announceTime = 0,
        isMealActive = true,
        mealTime = 0
    )

    val FAKULTE_LOGOLAR = ArrayList<Int>().apply {
        add(R.drawable.muhendislik)
        add(R.drawable.dis)
        add(R.drawable.eczacilik)
        add(R.drawable.edebiyat)
        add(R.drawable.egitim)
        add(R.drawable.fen_banner_tr)
        add(R.drawable.sanatlar)
        add(R.drawable.havacilik)
        add(R.drawable.hukuk)
        add(R.drawable.iibf)
        add(R.drawable.ilahiyat)
        add(R.drawable.saglikbilimleri)
        add(R.drawable.besyo)
        add(R.drawable.turizm)
        add(R.drawable.veteriner)
    }

    val MUH_LOGOLAR = ArrayList<Int>().apply {
        add(R.drawable.bilgisayar)
        add(R.drawable.biyomedikal)
        add(R.drawable.cevre)
        add(R.drawable.elektrik)
        add(R.drawable.endustri)
        add(R.drawable.tasarim)
        add(R.drawable.enerji)
        add(R.drawable.gida)
        add(R.drawable.harita)
        add(R.drawable.insaat)
        add(R.drawable.makine)
        add(R.drawable.malzeme)
        add(R.drawable.mekatronik)
        add(R.drawable.tekstil)

    }

    val FAKULTELER = ArrayList<String>()
        .apply {
            add("Mühendislik Fakültesi")
            add("Diş Hekimliği Fakültesi")
            add("Eczacılık Fakültesi ")
            add("Edebiyat Fakültesi")
            add("Eğitim Fakültesi")
            //Fen Fakültesi
            add("Fen Fakültesi")
            add("Güzel Sanatlar Fakültesi")
            add("Havacılık Ve Uzay Bilimleri")
            add("Hukuk Fakültesi")
            add("İktisadi Ve İdari Bilimler \nFakültesi")
            add("İlahiyat Fakültesi")
            add("Sağlık Bilimleri Fakültesi")
            add("Spor Bilimleri Fakültesi")
            add("Turizm Fakültesi")
            add("Veterinerlik Fakültesi")
        }

    private val FAKULTE_LINKLER = ArrayList<String>()
        .apply {
            add("https://mf.erciyes.edu.tr/")
            add("https://dent.erciyes.edu.tr")
            add("https://pharmacy.erciyes.edu.tr/")
            add("https://edebiyat.erciyes.edu.tr/")
            add("http://egitim.erciyes.edu.tr/announcement/1/1/")
            //http://fen.erciyes.edu.tr/
            add("http://fen.erciyes.edu.tr/")
            // add("https://fbe.erciyes.edu.tr/")
            add("http://guzelsanat.erciyes.edu.tr/Duyurular")
            add("https://havacilik.erciyes.edu.tr")
            add("https://hukuk.erciyes.edu.tr")
            add("http://iibf.erciyes.edu.tr")
            add("http://ilahiyat.erciyes.edu.tr")
            // add("http://iletisim.erciyes.edu.tr/category/duyuru/")
            // add("http://mimarlik.erciyes.edu.tr/")
            add("http://sbf.erciyes.edu.tr")
            // add("http://ziraat.erciyes.edu.tr/")
            add("http://spbf.erciyes.edu.tr")
            // add("http://tip.erciyes.edu.tr/")
            add("http://turizm.erciyes.edu.tr")
            add("http://veteriner.erciyes.edu.tr")
        }
    private val FAKULTELER_SELECT_0 = ArrayList<String>()
        .apply {
            add("#duytabALL > div > div.col-md-12 > table > tbody > tr:nth-child(")
            add("#event > div > div > div > div:nth-child(1) > article:nth-child(")
            add("#Duyurular > li:nth-child(")//eczacılık
            add("#Duyurular > li:nth-child(")
            add("#list-view > div:nth-child(")
            //#Duyurular > li:nth-child(
            add("#Duyurular > li:nth-child(")
            // add("#Duyurular > li:nth-child(")
            add("body > section.inside-content > div > div > div > div:nth-child(")
            add("#Duyurular > li:nth-child(")
            add("#Duyurular > li:nth-child(")
            add("#news > div.container > div > div.col-md-6.blog-top-right-grid > div.Categories > ul > li:nth-child(")
            add("#Duyurular > li:nth-child(")
            add("#Duyurular > li:nth-child(")
            add("#Duyurular > li:nth-child(")
            add("#Duyurular > li:nth-child(")
            add("#ContentPlaceHolder1_divDuyuru > div:nth-child(")
        }

    private val FAKULTELER_SELECT_1 = ArrayList<String>()
        .apply {
            add(") > td:nth-child(2) > h5")
            add(") > div > h5")
            add(") > span.aciklama > a")//ecza
            add(") > span.aciklama")
            add(") > div > div > div > h3")
            //) > span.aciklama > a
            add(") > span.aciklama > a")
            //  add(") > span.aciklama")
            add(") > div.col-xs-8.col-sm-9 > h2 > a")
            add(") > span.aciklama")
            add(") > span.aciklama")
            add(") > a")
            add(") > span.aciklama")
            add(") > span.aciklama > a")
            add(") > span.aciklama > a")
            add(") > span.aciklama")
            add(")")
        }

    private val FAKULTELER_INDEX_SELECT = ArrayList<String>()
        .apply {
            add("#wrapper > div > section:nth-child(3) > div > div > div > div > article > div > div.mt-10")
            add("#wrapper > div.main-content > section:nth-child(2) > div > div > div.col-sm-12.col-md-9")
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok")
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok")
            add("#wrapper > div.courses-page-area4 > div > div")
            //#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok")
            add("body > section.inside-content > div > div:nth-child(1)")
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok")
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok > p > a")
            add("body > div.blog > div > div.col-md-8.blog-top-left-grid")
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok")
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok")
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok")
            add("#ContentPlaceHolder1_ContentPlaceHolder1_ctl00_DuyuruDetayBlok")
            add("#form1 > div.wrapper > div.bg-color-light > div")
        }


    val MUH_BOLUMLER = ArrayList<String>()
        .apply {
            add("Bilgisayar Mühendisliği")
            add("Biyomedikal Mühendisliği")
            add("Çevre Mühendisliği")
            add("Elektrik-Elektronik Mühendisliği")
            add("Endüstri Mühendisliği")
            add("Endüstriyel Tasarım Mühendisliği")
            add("Enerji Sistemleri Mühendisliği")
            add("Gıda Mühendisliği")
            add("Harita Mühendisliği")
            add("İnşaat Mühendisliği")
            add("Makine Mühendisliği")
            add("Malzeme Bilimi ve Mühendisliği")
            add("Mekatronik Mühendisliği")
            add("Tekstil Mühendisliği")
        }

    private val MUH_LINKLER = ArrayList<String>()
        .apply {
            add("https://bm.erciyes.edu.tr/")
            add("https://biomed.erciyes.edu.tr/")
            add("https://cevre.erciyes.edu.tr/")
            add("https://em.erciyes.edu.tr/")
            add("https://endustri.erciyes.edu.tr/")
            add("https://etm.erciyes.edu.tr/")
            add("https://esm.erciyes.edu.tr/")
            add("https://gida.erciyes.edu.tr/")
            add("https://harita.erciyes.edu.tr/")
            add("https://insaat.erciyes.edu.tr/")
            add("https://me.erciyes.edu.tr/")
            add("https://mbm.erciyes.edu.tr/")
            add("https://mekatronik.erciyes.edu.tr/")
            add("https://tekstil.erciyes.edu.tr/")
        }
    private const val MUH_SELECT_0 =
        "#duytabALL > div > div.col-md-12 > table > tbody > tr:nth-child("
    private const val MUH_SELECT_1 = ")"
    private const val MUH_INDEX_SELECT =
        "#wrapper > div > section:nth-child(3) > div > div > div > div > article > div > div.mt-10"

}
