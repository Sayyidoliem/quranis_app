package com.example.quranisapp.data.kotpref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref

object SettingPreferences: KotprefModel() {
    //buat ganti tranlate
    const val INDONESIA = 0
    const val ENGLISH = 1

    private const val LIGHT_MODE = false

    const val FOCUS_READ = false

    var isSelectedLanguage by intPref(
      INDONESIA
    )

    var isDarkMode by booleanPref(
        LIGHT_MODE
    )

    var isVisibleTranslate by booleanPref(
        FOCUS_READ
    )

    var dateFormat by longPref(-1)

    var selectedQori by enumOrdinalPref(
        Qories.ABDUL_SOMAD
    )

    var isOnBoarding by booleanPref(true)

}

enum class Qories(val id : String, val qoriName : String, val qoriImage : String){
    ABDUL_SOMAD("AbdulSamad_64kbps_QuranExplorer.Com", "Abdul Samad", "https://th.bing.com/th/id/OIP.7BHZsSPXgBg9yHKJBG1dSwHaHa?pid=ImgDet&rs=1"),
    ABDUL_AWAAD("Abdullaah_3awwaad_Al-Juhaynee_128kbps", "Abdullah Awaad Al-Juhaynee", "https://arynews.tv/wp-content/uploads/2019/04/Abdullah-Awad-Al-Juhany.jpg"),
    ABDUL_BASFAR("Abdullah_Basfar_192kbps", "Abdullah Basfar","https://th.bing.com/th/id/R.ea60c9ffb69e45324c01fee42cf43794?rik=Pkn8u5OfBP5uDA&riu=http%3a%2f%2fquran.com.kw%2fen%2fwp-content%2fuploads%2fabdullah-basfar.jpg&ehk=lSOodZEfR8056uscDMjb7olJ5LJzxjboNGnj%2bwF6WY4%3d&risl=&pid=ImgRaw&r=0", ),
    ABDUL_MATROUD("Abdullah_Matroud_128kbps", "Abdullah Matroud", "https://api.quranpro.co/images/QWJkdWxsYWggQWwgTWF0cm9vZEAzeC5wbmc%3D"),
    ABDUR_ASSUDAIS("Abdurrahmaan_As-Sudais_192kbps", "Abdurrahman As-Sudais", "https://th.bing.com/th/id/R.3d917aef773ad0f69ebb1c2347ea19a3?rik=gKgZTv2JOyBT6Q&riu=http%3a%2f%2f3.bp.blogspot.com%2f-G6TmKAfRYKU%2fT1uc3PLVkwI%2fAAAAAAAAD-c%2fhHKziLSawDw%2fs1600%2fabdul-rahman-al-sudais-81.jpg&ehk=j6I3atYN7xqEFe1MQW9pyiukC51ZL%2b0%2fwmMjbw1uJVM%3d&risl=&pid=ImgRaw&r=0"),
    ABU_BAKR_ASHSHAATREE("Abu_Bakr_Ash-Shaatree_128kbps","Abu Bakr Ash-Shaatree",""),
    AHMED_NEANA("Ahmed_Neana_128kbps","Ahmed Neana", ""),
    AHMED_IBN_ALI_AL_AJAMY("Ahmed_ibn_Ali_al-Ajamy_128kbps_ketaballah.net","Ahmed Ibn Ali Al-Ajamy", ""),
    AKRAM_AL_ALAQIMY("Akram_AlAlaqimy_128kbps","Akram Al Alaqimy", ""),
    AL_AFASY("Alafasy_128kbps","Al-Afasy", ""),
    ALI_HAJJAJ_AL_SUESY("Ali_Hajjaj_AlSuesy_128kbps", "Ali Hajjaj Al-Suesy", ""),
    ALI_JABER("Ali_Jaber_64kbps", "ALi Jaber", ""),
    AYMAN_SOWAID("Ayman_Sowaid_64kbps", "Ayman Sowaid", ""),
    FARES_ABBAD("Fares_Abbad_64kbps", "Fares Abbad", ""),
    GHAMADI("Ghamadi_40kbps", "Ghamadi", ""),
    HANI_RIFAI("Hani_Rifai_192kbps", "Hani Rhifai", ""),
    HUDAIFY("Hudhaify_128kbps", "Hudaifi", ""),
    AL_HUSAIRY("Husary_128kbps", "Husairy", ""),
    IBRAHIM_AKHDAR("Ibrahim_Akhdar_32kbps", "Ibrahim Akhdar", ""),
    KARIM_MANSOORI("Karim_Mansoori_40kbps", "Karim Mansuri", ""),
    KHALID_ABDULLAH_AL_QAHTANEE("Khaalid_Abdullaah_al-Qahtaanee_192kbps","Khalid Abdullah Al-Qahtanee",""),
    MAHER_AL_MUAIQLY("Maher_AlMuaiqly_64kbps", "Maher Al-Muaqly", ""),
    AL_MENSHAWI("Menshawi_32kbps", "Al-Menshawi", ""),
    AL_MINSHAWY("Minshawy_Murattal_128kbps", "Al-Minshawy", ""),
    MOHAMMAD_AL_TABLAWAY("Mohammad_al_Tablaway_64kbps", "Mohammad Al-Tablaway", ""),
    SHAMSAD_ALI_KHAN("urdu_shamshad_ali_khan_46kbps","Samsad Ali Khan", ""),
    ABDUL_BASIT("warsh_Abdul_Basit_128kbps", "Abdul Basit", ""),
    IBRAHIM_AL_DOSARY("warsh_ibrahim_aldosary_128kbps","Ibrahim Al Dosary", ""),
    YASSIN_AL_JAZAERI("warsh_yassin_al_jazaery_64kbps","Yassin Al Jazaeri", "")
}