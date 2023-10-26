package com.example.quranisapp.data.kotpref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref

object SettingPreferences: KotprefModel() {
    //buat ganti tranlate
    const val INDONESIA = 0
    const val ENGLISH = 1

    private const val LIGHT_MODE = false
    private const val DARK_MODE = false

    const val FOCUS_READ = false

    var isSelectedLanguage by intPref(
      INDONESIA
    )

    var isDarkMode by booleanPref(
        LIGHT_MODE
    )

    var selectedQori by enumOrdinalPref(
        Qories.ABDUL_SOMAD
    )

}

enum class Qories(val id : String, val qoriName : String, val qoriImage : String){
    ABDUL_SOMAD("AbdulSamad_64kbps_QuranExplorer.Com", "Abdul Somad", "https://th.bing.com/th/id/OIP.7BHZsSPXgBg9yHKJBG1dSwHaHa?pid=ImgDet&rs=1"),
    ABDUL_AWAAD("Abdullaah_3awwaad_Al-Juhaynee_128kbps", "Abdullah Awaad Al-Juhaynee", "https://arynews.tv/wp-content/uploads/2019/04/Abdullah-Awad-Al-Juhany.jpg"),
    ABDUL_BASFAR("Abdullah_Basfar_192kbps", "Abdul Basfar","https://th.bing.com/th/id/R.ea60c9ffb69e45324c01fee42cf43794?rik=Pkn8u5OfBP5uDA&riu=http%3a%2f%2fquran.com.kw%2fen%2fwp-content%2fuploads%2fabdullah-basfar.jpg&ehk=lSOodZEfR8056uscDMjb7olJ5LJzxjboNGnj%2bwF6WY4%3d&risl=&pid=ImgRaw&r=0", ),
    ABDUL_MATROUD("Abdullah_Matroud_128kbps", "Abdul Matroud", "https://api.quranpro.co/images/QWJkdWxsYWggQWwgTWF0cm9vZEAzeC5wbmc%3D"),
    ABDUR_ASSUDAIS("Abdurrahmaan_As-Sudais_192kbps", "Abdurrahman As-Sudais", "https://th.bing.com/th/id/R.3d917aef773ad0f69ebb1c2347ea19a3?rik=gKgZTv2JOyBT6Q&riu=http%3a%2f%2f3.bp.blogspot.com%2f-G6TmKAfRYKU%2fT1uc3PLVkwI%2fAAAAAAAAD-c%2fhHKziLSawDw%2fs1600%2fabdul-rahman-al-sudais-81.jpg&ehk=j6I3atYN7xqEFe1MQW9pyiukC51ZL%2b0%2fwmMjbw1uJVM%3d&risl=&pid=ImgRaw&r=0"),
    ABDUR_ASSUDAIS1("Abdurrahmaan_As-Sudais_192kbps", "Abdurrahman As-Sudais", "https://th.bing.com/th/id/R.3d917aef773ad0f69ebb1c2347ea19a3?rik=gKgZTv2JOyBT6Q&riu=http%3a%2f%2f3.bp.blogspot.com%2f-G6TmKAfRYKU%2fT1uc3PLVkwI%2fAAAAAAAAD-c%2fhHKziLSawDw%2fs1600%2fabdul-rahman-al-sudais-81.jpg&ehk=j6I3atYN7xqEFe1MQW9pyiukC51ZL%2b0%2fwmMjbw1uJVM%3d&risl=&pid=ImgRaw&r=0"),
    ABDUR_ASSUDAIS2("Abdurrahmaan_As-Sudais_192kbps", "Abdurrahman As-Sudais", "https://th.bing.com/th/id/R.3d917aef773ad0f69ebb1c2347ea19a3?rik=gKgZTv2JOyBT6Q&riu=http%3a%2f%2f3.bp.blogspot.com%2f-G6TmKAfRYKU%2fT1uc3PLVkwI%2fAAAAAAAAD-c%2fhHKziLSawDw%2fs1600%2fabdul-rahman-al-sudais-81.jpg&ehk=j6I3atYN7xqEFe1MQW9pyiukC51ZL%2b0%2fwmMjbw1uJVM%3d&risl=&pid=ImgRaw&r=0"),
    ABDUR_ASSUDAIS3("Abdurrahmaan_As-Sudais_192kbps", "Abdurrahman As-Sudais", "https://th.bing.com/th/id/R.3d917aef773ad0f69ebb1c2347ea19a3?rik=gKgZTv2JOyBT6Q&riu=http%3a%2f%2f3.bp.blogspot.com%2f-G6TmKAfRYKU%2fT1uc3PLVkwI%2fAAAAAAAAD-c%2fhHKziLSawDw%2fs1600%2fabdul-rahman-al-sudais-81.jpg&ehk=j6I3atYN7xqEFe1MQW9pyiukC51ZL%2b0%2fwmMjbw1uJVM%3d&risl=&pid=ImgRaw&r=0"),
    ABDUR_ASSUDAIS4("Abdurrahmaan_As-Sudais_192kbps", "Abdurrahman As-Sudais", "https://th.bing.com/th/id/R.3d917aef773ad0f69ebb1c2347ea19a3?rik=gKgZTv2JOyBT6Q&riu=http%3a%2f%2f3.bp.blogspot.com%2f-G6TmKAfRYKU%2fT1uc3PLVkwI%2fAAAAAAAAD-c%2fhHKziLSawDw%2fs1600%2fabdul-rahman-al-sudais-81.jpg&ehk=j6I3atYN7xqEFe1MQW9pyiukC51ZL%2b0%2fwmMjbw1uJVM%3d&risl=&pid=ImgRaw&r=0"),
}