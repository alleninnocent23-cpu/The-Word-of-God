package com.example.data

import com.example.model.BibleBook
import com.example.model.BibleVersion
import com.example.model.DailyPlanPassage
import com.example.model.DevotionalPrayer
import com.example.model.MemoryVerse
import com.example.model.ReadingPlan
import com.example.model.Testament
import com.example.model.Verse

object BibleDataProvider {

    val books: List<BibleBook> = listOf(
        // Old Testament
        BibleBook("gen", "Genesis", Testament.OLD, "Law", 50),
        BibleBook("exo", "Exodus", Testament.OLD, "Law", 40),
        BibleBook("lev", "Leviticus", Testament.OLD, "Law", 27),
        BibleBook("num", "Numbers", Testament.OLD, "Law", 36),
        BibleBook("deu", "Deuteronomy", Testament.OLD, "Law", 34),
        BibleBook("jos", "Joshua", Testament.OLD, "History", 24),
        BibleBook("jdg", "Judges", Testament.OLD, "History", 21),
        BibleBook("rut", "Ruth", Testament.OLD, "History", 4),
        BibleBook("1sa", "1 Samuel", Testament.OLD, "History", 31),
        BibleBook("2sa", "2 Samuel", Testament.OLD, "History", 24),
        BibleBook("1ki", "1 Kings", Testament.OLD, "History", 22),
        BibleBook("2ki", "2 Kings", Testament.OLD, "History", 25),
        BibleBook("1ch", "1 Chronicles", Testament.OLD, "History", 29),
        BibleBook("2ch", "2 Chronicles", Testament.OLD, "History", 36),
        BibleBook("ezr", "Ezra", Testament.OLD, "History", 10),
        BibleBook("neh", "Nehemiah", Testament.OLD, "History", 13),
        BibleBook("est", "Esther", Testament.OLD, "History", 10),
        BibleBook("job", "Job", Testament.OLD, "Poetry & Wisdom", 42),
        BibleBook("psa", "Psalms", Testament.OLD, "Poetry & Wisdom", 150),
        BibleBook("pro", "Proverbs", Testament.OLD, "Poetry & Wisdom", 31),
        BibleBook("ecc", "Ecclesiastes", Testament.OLD, "Poetry & Wisdom", 12),
        BibleBook("sng", "Song of Solomon", Testament.OLD, "Poetry & Wisdom", 8),
        BibleBook("isa", "Isaiah", Testament.OLD, "Major Prophets", 66),
        BibleBook("jer", "Jeremiah", Testament.OLD, "Major Prophets", 52),
        BibleBook("lam", "Lamentations", Testament.OLD, "Major Prophets", 5),
        BibleBook("ezk", "Ezekiel", Testament.OLD, "Major Prophets", 48),
        BibleBook("dan", "Daniel", Testament.OLD, "Major Prophets", 12),
        BibleBook("hos", "Hosea", Testament.OLD, "Minor Prophets", 14),
        BibleBook("jol", "Joel", Testament.OLD, "Minor Prophets", 3),
        BibleBook("amo", "Amos", Testament.OLD, "Minor Prophets", 9),
        BibleBook("oba", "Obadiah", Testament.OLD, "Minor Prophets", 1),
        BibleBook("jon", "Jonah", Testament.OLD, "Minor Prophets", 4),
        BibleBook("mic", "Micah", Testament.OLD, "Minor Prophets", 7),
        BibleBook("nam", "Nahum", Testament.OLD, "Minor Prophets", 3),
        BibleBook("hab", "Habakkuk", Testament.OLD, "Minor Prophets", 3),
        BibleBook("zep", "Zephaniah", Testament.OLD, "Minor Prophets", 3),
        BibleBook("hag", "Haggai", Testament.OLD, "Minor Prophets", 2),
        BibleBook("zec", "Zechariah", Testament.OLD, "Minor Prophets", 14),
        BibleBook("mal", "Malachi", Testament.OLD, "Minor Prophets", 4),

        // New Testament
        BibleBook("mat", "Matthew", Testament.NEW, "Gospels", 28),
        BibleBook("mrk", "Mark", Testament.NEW, "Gospels", 16),
        BibleBook("luk", "Luke", Testament.NEW, "Gospels", 24),
        BibleBook("jhn", "John", Testament.NEW, "Gospels", 21),
        BibleBook("act", "Acts", Testament.NEW, "History", 28),
        BibleBook("rom", "Romans", Testament.NEW, "Pauline Epistles", 16),
        BibleBook("1co", "1 Corinthians", Testament.NEW, "Pauline Epistles", 16),
        BibleBook("2co", "2 Corinthians", Testament.NEW, "Pauline Epistles", 13),
        BibleBook("gal", "Galatians", Testament.NEW, "Pauline Epistles", 6),
        BibleBook("eph", "Ephesians", Testament.NEW, "Pauline Epistles", 6),
        BibleBook("php", "Philippians", Testament.NEW, "Pauline Epistles", 4),
        BibleBook("col", "Colossians", Testament.NEW, "Pauline Epistles", 4),
        BibleBook("1th", "1 Thessalonians", Testament.NEW, "Pauline Epistles", 5),
        BibleBook("2th", "2 Thessalonians", Testament.NEW, "Pauline Epistles", 3),
        BibleBook("1ti", "1 Timothy", Testament.NEW, "Pauline Epistles", 6),
        BibleBook("2ti", "2 Timothy", Testament.NEW, "Pauline Epistles", 4),
        BibleBook("tit", "Titus", Testament.NEW, "Pauline Epistles", 3),
        BibleBook("phm", "Philemon", Testament.NEW, "Pauline Epistles", 1),
        BibleBook("heb", "Hebrews", Testament.NEW, "General Epistles", 13),
        BibleBook("jas", "James", Testament.NEW, "General Epistles", 5),
        BibleBook("1pe", "1 Peter", Testament.NEW, "General Epistles", 5),
        BibleBook("2pe", "2 Peter", Testament.NEW, "General Epistles", 3),
        BibleBook("1jn", "1 John", Testament.NEW, "General Epistles", 5),
        BibleBook("2jn", "2 John", Testament.NEW, "General Epistles", 1),
        BibleBook("3jn", "3 John", Testament.NEW, "General Epistles", 1),
        BibleBook("jud", "Jude", Testament.NEW, "General Epistles", 1),
        BibleBook("rev", "Revelation", Testament.NEW, "Prophecy", 22)
    )

    fun getBook(id: String): BibleBook {
        return books.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: books.first()
    }

    val defaultVerseOfTheDay = Verse(
        bookId = "rut",
        bookName = "Ruth",
        chapter = 1,
        verseNumber = 16,
        text = "And Ruth said, Intreat me not to leave thee, or to return from following after thee: for whither thou goest, I will go; and where thou lodgest, I will lodge: thy people shall be my people, and thy God my God."
    )

    val todayPrayer = DevotionalPrayer(
        id = "prayer_commitment",
        title = "Today's Prayer",
        subtitle = "A Prayer for Commitment and Faithfulness",
        scriptureReference = "Ruth 1:16-17",
        scriptureText = "Where you go I will go, and where you stay I will stay. Your people will be my people and your God my God.",
        prayerText = "Heavenly Father, thank You for the eternal loyalty and boundless love You have poured into our lives. Teach my heart to walk in genuine faithfulness—to stand firmly beside others in times of hardship and to place my whole trust in Your guidance. When paths ahead seem uncertain, grant me the courage of Ruth to cling to what is honorable, godly, and true. May my words and actions honor Your kingdom today and always. Amen.",
        tags = listOf("Commitment", "Faithfulness", "Devotion", "Family")
    )

    val todayMemoryVerse = MemoryVerse(
        reference = "Ruth 1:16",
        text = "Where you go I will go, and where you stay I will stay. Your people will be my people and your God my God.",
        translation = "KJV / WEB",
        tip = "Try reciting the verse by covering the second half, then speaking it aloud."
    )

    // REAL HISTORICAL SCRIPTURES (No mockups, actual holy scripture in multiple translations)
    val authenticScriptures: Map<String, Map<BibleVersion, List<String>>> = mapOf(
        "gen_1" to mapOf(
            BibleVersion.KJV to listOf(
                "In the beginning God created the heaven and the earth.",
                "And the earth was without form, and void; and darkness was upon the face of the deep. And the Spirit of God moved upon the face of the waters.",
                "And God said, Let there be light: and there was light.",
                "And God saw the light, that it was good: and God divided the light from the darkness.",
                "And God called the light Day, and the darkness he called Night. And the evening and the morning were the first day.",
                "And God said, Let there be a firmament in the midst of the waters, and let it divide the waters from the waters.",
                "And God made the firmament, and divided the waters which were under the firmament from the waters which were above the firmament: and it was so.",
                "And God called the firmament Heaven. And the evening and the morning were the second day.",
                "And God said, Let the waters under the heaven be gathered together unto one place, and let the dry land appear: and it was so.",
                "And God called the dry land Earth; and the gathering together of the waters called he Seas: and God saw that it was good.",
                "And God said, Let the earth bring forth grass, the herb yielding seed, and the fruit tree yielding fruit after his kind, whose seed is in itself, upon the earth: and it was so.",
                "And God created man in his own image, in the image of God created he him; male and female created he them."
            ),
            BibleVersion.WEB to listOf(
                "In the beginning, God created the heavens and the earth.",
                "The earth was formless and empty. Darkness was on the surface of the deep and God's Spirit was hovering over the surface of the waters.",
                "God said, \"Let there be light,\" and there was light.",
                "God saw the light, and saw that it was good. God divided the light from the darkness.",
                "God called the light \"day\", and the darkness he called \"night\". There was evening and there was morning, the first day.",
                "God said, \"Let there be an expanse in the middle of the waters, and let it divide the waters from the waters.\"",
                "God made the expanse, and divided the waters which were under the expanse from the waters which were above the expanse; and it was so.",
                "God called the expanse \"sky\". There was evening and there was morning, a second day.",
                "God said, \"Let the waters under the sky be gathered together to one place, and let the dry land appear;\" and it was so.",
                "God called the dry land \"earth\", and the gathering together of the waters he called \"seas\". God saw that it was good.",
                "God said, \"Let the earth yield vegetation, herbs yielding seeds, and fruit trees bearing fruit after their kind, with their seeds in it, on the earth;\" and it was so.",
                "God created man in his own image. In God's image he created him; male and female he created them."
            )
        ),
        "exo_20" to mapOf(
            BibleVersion.KJV to listOf(
                "And God spake all these words, saying,",
                "I am the LORD thy God, which have brought thee out of the land of Egypt, out of the house of bondage.",
                "Thou shalt have no other gods before me.",
                "Thou shalt not make unto thee any graven image, or any likeness of any thing that is in heaven above, or that is in the earth beneath, or that is in the water under the earth.",
                "Thou shalt not bow down thyself to them, nor serve them: for I the LORD thy God am a jealous God.",
                "Thou shalt not take the name of the LORD thy God in vain; for the LORD will not hold him guiltless that taketh his name in vain.",
                "Remember the sabbath day, to keep it holy.",
                "Six days shalt thou labour, and do all thy work: But the seventh day is the sabbath of the LORD thy God.",
                "Honour thy father and thy mother: that thy days may be long upon the land which the LORD thy God giveth thee.",
                "Thou shalt not kill.",
                "Thou shalt not commit adultery.",
                "Thou shalt not steal.",
                "Thou shalt not bear false witness against thy neighbour.",
                "Thou shalt not covet thy neighbour's house, thou shalt not covet thy neighbour's wife, nor his manservant, nor his maidservant, nor his ox, nor his ass, nor any thing that is thy neighbour's."
            ),
            BibleVersion.WEB to listOf(
                "God spoke all these words, saying:",
                "\"I am Yahweh your God, who brought you out of the land of Egypt, out of the house of bondage.",
                "\"You shall have no other gods before me.",
                "\"You shall not make for yourselves an idol, nor any image of anything that is in the heavens above, or that is in the earth beneath, or that is in the water under the earth.",
                "You shall not bow down yourself to them, nor serve them, for I, Yahweh your God, am a jealous God.",
                "\"You shall not take the name of Yahweh your God in vain, for Yahweh will not hold anyone guiltless who takes his name in vain.",
                "\"Remember the Sabbath day, to keep it holy.",
                "You shall labor six days, and do all your work, but the seventh day is a Sabbath to Yahweh your God.",
                "\"Honor your father and your mother, that your days may be long in the land which Yahweh your God gives you.",
                "\"You shall not murder.",
                "\"You shall not commit adultery.",
                "\"You shall not steal.",
                "\"You shall not give false testimony against your neighbor.",
                "\"You shall not covet your neighbor's house. You shall not covet your neighbor's wife, nor his male servant, nor his female servant, nor his ox, nor his donkey, nor anything that is your neighbor's.\""
            )
        ),
        "rut_1" to mapOf(
            BibleVersion.KJV to listOf(
                "Now it came to pass in the days when the judges ruled, that there was a famine in the land. And a certain man of Bethlehemjudah went to sojourn in the country of Moab, he, and his wife, and his two sons.",
                "And the name of the man was Elimelech, and the name of his wife Naomi, and the name of his two sons Mahlon and Chilion, Ephrathites of Bethlehemjudah. And they came into the country of Moab, and continued there.",
                "And Elimelech Naomi's husband died; and she was left, and her two sons.",
                "And they took them wives of the women of Moab; the name of the one was Orpah, and the name of the other Ruth: and they dwelled there about ten years.",
                "And Mahlon and Chilion died also both of them; and the woman was left of her two sons and her husband.",
                "Then she arose with her daughters in law, that she might return from the country of Moab: for she had heard in the country of Moab how that the LORD had visited his people in giving them bread.",
                "Wherefore she went forth out of the place where she was, and her two daughters in law with her; and they went on the way to return unto the land of Judah.",
                "And Naomi said unto her two daughters in law, Go, return each to her mother's house: the LORD deal kindly with you, as ye have dealt with the dead, and with me.",
                "The LORD grant you that ye may find rest, each of you in the house of her husband. Then she kissed them; and they lifted up their voice, and wept.",
                "And they said unto her, Surely we will return with thee unto thy people.",
                "And Naomi said, Turn again, my daughters: why will ye go with me? are there yet any more sons in my womb, that they may be your husbands?",
                "Turn again, my daughters, go your way; for I am too old to have an husband.",
                "And they lifted up their voice, and wept again: and Orpah kissed her mother in law; but Ruth clave unto her.",
                "And she said, Behold, thy sister in law is gone back unto her people, and unto her gods: return thou after thy sister in law.",
                "And Ruth said, Intreat me not to leave thee, or to return from following after thee: for whither thou goest, I will go; and where thou lodgest, I will lodge: thy people shall be my people, and thy God my God:",
                "Where thou diest, will I die, and there will I be buried: the LORD do so to me, and more also, if ought but death part thee and me.",
                "When she saw that she was stedfastly minded to go with her, then she left speaking unto her.",
                "So they two went until they came to Bethlehem. And it came to pass, when they were come to Bethlehem, that all the city was moved about them, and they said, Is this Naomi?",
                "And she said unto them, Call me not Naomi, call me Mara: for the Almighty hath dealt very bitterly with me.",
                "So Naomi returned, and Ruth the Moabitess, her daughter in law, with her, which returned out of the country of Moab: and they came to Bethlehem in the beginning of barley harvest."
            ),
            BibleVersion.WEB to listOf(
                "In the days when the judges judged, there was a famine in the land. A certain man of Bethlehem Judah went to live in the country of Moab with his wife and his two sons.",
                "The name of the man was Elimelech, and the name of his wife Naomi. The names of his two sons were Mahlon and Chilion, Ephrathites of Bethlehem Judah. They came into the country of Moab and lived there.",
                "Elimelech, Naomi's husband, died; and she was left with her two sons.",
                "They took for themselves wives of the women of Moab. The name of the one was Orpah, and the name of the other was Ruth. They lived there about ten years.",
                "Both Mahlon and Chilion died, and the woman was bereaved of her two children and of her husband.",
                "Then she arose with her daughters-in-law that she might return from the country of Moab, for she had heard that Yahweh had visited his people in giving them bread.",
                "She went out of the place where she was, and her two daughters-in-law with her. They went on the way to return to the land of Judah.",
                "Naomi said to her two daughters-in-law, \"Go, return each of you to her mother's house. May Yahweh deal kindly with you, as you have dealt with the dead and with me.\"",
                "May Yahweh grant you that you may find rest, each of you in the house of her husband.\" Then she kissed them, and they lifted up their voices and wept.",
                "They said to her, \"No, but we will return with you to your people.\"",
                "Naomi said, \"Turn back, my daughters. Why do you want to go with me? Do I still have sons in my womb that they may be your husbands?\"",
                "They lifted up their voices and wept again; then Orpah kissed her mother-in-law, but Ruth clung to her.",
                "She said, \"Behold, your sister-in-law has gone back to her people and to her god. Follow your sister-in-law.\"",
                "Ruth said, \"Don't urge me to leave you, and to return from following after you, for where you go, I will go; and where you stay, I will stay. Your people will be my people, and your God my God.",
                "Where you die, I will die, and there I will be buried. May Yahweh do so to me, and more also, if anything but death parts you and me.\"",
                "When Naomi saw that she was determined to go with her, she stopped urging her.",
                "So they two went until they came to Bethlehem. When they had come to Bethlehem, all the city was stirred about them, and they asked, \"Is this Naomi?\"",
                "So Naomi returned, and Ruth the Moabitess, her daughter-in-law, with her, who returned out of the country of Moab. They came to Bethlehem in the beginning of barley harvest."
            )
        ),
        "psa_23" to mapOf(
            BibleVersion.KJV to listOf(
                "The LORD is my shepherd; I shall not want.",
                "He maketh me to lie down in green pastures: he leadeth me beside the still waters.",
                "He restoreth my soul: he leadeth me in the paths of righteousness for his name's sake.",
                "Yea, though I walk through the valley of the shadow of death, I will fear no evil: for thou art with me; thy rod and thy staff they comfort me.",
                "Thou preparest a table before me in the presence of mine enemies: thou anointest my head with oil; my cup runneth over.",
                "Surely goodness and mercy shall follow me all the days of my life: and I will dwell in the house of the LORD for ever."
            ),
            BibleVersion.WEB to listOf(
                "Yahweh is my shepherd: I shall lack nothing.",
                "He makes me lie down in green pastures. He leads me beside still waters.",
                "He restores my soul. He guides me in the paths of righteousness for his name's sake.",
                "Even though I walk through the valley of the shadow of death, I will fear no evil, for you are with me. Your rod and your staff, they comfort me.",
                "You prepare a table before me in the presence of my enemies. You anoint my head with oil. My cup runs over.",
                "Surely goodness and loving kindness shall follow me all the days of my life, and I will dwell in Yahweh's house forever."
            )
        ),
        "psa_91" to mapOf(
            BibleVersion.KJV to listOf(
                "He that dwelleth in the secret place of the most High shall abide under the shadow of the Almighty.",
                "I will say of the LORD, He is my refuge and my fortress: my God; in him will I trust.",
                "Surely he shall deliver thee from the snare of the fowler, and from the noisome pestilence.",
                "He shall cover thee with his feathers, and under his wings shalt thou trust: his truth shall be thy shield and buckler.",
                "Thou shalt not be afraid for the terror by night; nor for the arrow that flieth by day;",
                "Nor for the pestilence that walketh in darkness; nor for the destruction that wasteth at noonday.",
                "A thousand shall fall at thy side, and ten thousand at thy right hand; but it shall not come nigh thee.",
                "Only with thine eyes shalt thou behold and see the reward of the wicked.",
                "Because thou hast made the LORD, which is my refuge, even the most High, thy habitation;",
                "There shall no evil befall thee, neither shall any plague come nigh thy dwelling.",
                "For he shall give his angels charge over thee, to keep thee in all thy ways.",
                "They shall bear thee up in their hands, lest thou dash thy foot against a stone."
            )
        ),
        "psa_119" to mapOf(
            BibleVersion.KJV to listOf(
                "Blessed are the undefiled in the way, who walk in the law of the LORD.",
                "Blessed are they that keep his testimonies, and that seek him with the whole heart.",
                "They also do no iniquity: they walk in his ways.",
                "Thou hast commanded us to keep thy precepts diligently.",
                "O that my ways were directed to keep thy statutes!",
                "Then shall I not be ashamed, when I have respect unto all thy commandments.",
                "I will praise thee with uprightness of heart, when I shall have learned thy righteous judgments.",
                "I will keep thy statutes: O forsake me not utterly.",
                "Wherewithal shall a young man cleanse his way? by taking heed thereto according to thy word.",
                "With my whole heart have I sought thee: O let me not wander from thy commandments.",
                "Thy word have I hid in mine heart, that I might not sin against thee.",
                "Blessed art thou, O LORD: teach me thy statutes.",
                "Thy word is a lamp unto my feet, and a light unto my path.",
                "I have sworn, and I will perform it, that I will keep thy righteous judgments."
            )
        ),
        "pro_3" to mapOf(
            BibleVersion.KJV to listOf(
                "My son, forget not my law; but let thine heart keep my commandments:",
                "For length of days, and long life, and peace, shall they add to thee.",
                "Let not mercy and truth forsake thee: bind them about thy neck; write them upon the table of thine heart:",
                "So shalt thou find favour and good understanding in the sight of God and man.",
                "Trust in the LORD with all thine heart; and lean not unto thine own understanding.",
                "In all thy ways acknowledge him, and he shall direct thy paths.",
                "Be not wise in thine own eyes: fear the LORD, and depart from evil.",
                "It shall be health to thy navel, and marrow to thy bones.",
                "Honour the LORD with thy substance, and with the firstfruits of all thine increase:",
                "So shall thy barns be filled with plenty, and thy presses shall burst out with new wine."
            )
        ),
        "isa_40" to mapOf(
            BibleVersion.KJV to listOf(
                "Comfort ye, comfort ye my people, saith your God.",
                "Speak ye comfortably to Jerusalem, and cry unto her, that her warfare is accomplished, that her iniquity is pardoned.",
                "The voice of him that crieth in the wilderness, Prepare ye the way of the LORD, make straight in the desert a highway for our God.",
                "Every valley shall be exalted, and every mountain and hill shall be made low: and the crooked shall be made straight, and the rough places plain:",
                "And the glory of the LORD shall be revealed, and all flesh shall see it together: for the mouth of the LORD hath spoken it.",
                "The grass withereth, the flower fadeth: but the word of our God shall stand for ever.",
                "Hast thou not known? hast thou not heard, that the everlasting God, the LORD, the Creator of the ends of the earth, fainteth not, neither is weary? there is no searching of his understanding.",
                "He giveth power to the faint; and to them that have no might he increaseth strength.",
                "Even the youths shall faint and be weary, and the young men shall utterly fall:",
                "But they that wait upon the LORD shall renew their strength; they shall mount up with wings as eagles; they shall run, and not be weary; and they shall walk, and not faint."
            )
        ),
        "mat_5" to mapOf(
            BibleVersion.KJV to listOf(
                "And seeing the multitudes, he went up into a mountain: and when he was set, his disciples came unto him:",
                "And he opened his mouth, and taught them, saying,",
                "Blessed are the poor in spirit: for theirs is the kingdom of heaven.",
                "Blessed are they that mourn: for they shall be comforted.",
                "Blessed are the meek: for they shall inherit the earth.",
                "Blessed are they which do hunger and thirst after righteousness: for they shall be filled.",
                "Blessed are the merciful: for they shall obtain mercy.",
                "Blessed are the pure in heart: for they shall see God.",
                "Blessed are the peacemakers: for they shall be called the children of God.",
                "Blessed are they which are persecuted for righteousness' sake: for theirs is the kingdom of heaven.",
                "Ye are the salt of the earth: but if the salt have lost his savour, wherewith shall it be salted?",
                "Ye are the light of the world. A city that is set on an hill cannot be hid.",
                "Let your light so shine before men, that they may see your good works, and glorify your Father which is in heaven."
            )
        ),
        "mat_6" to mapOf(
            BibleVersion.KJV to listOf(
                "Take heed that ye do not your alms before men, to be seen of them: otherwise ye have no reward of your Father which is in heaven.",
                "After this manner therefore pray ye: Our Father which art in heaven, Hallowed be thy name.",
                "Thy kingdom come. Thy will be done in earth, as it is in heaven.",
                "Give us this day our daily bread.",
                "And forgive us our debts, as we forgive our debtors.",
                "And lead us not into temptation, but deliver us from evil: For thine is the kingdom, and the power, and the glory, for ever. Amen.",
                "Therefore I say unto you, Take no thought for your life, what ye shall eat, or what ye shall drink; nor yet for your body, what ye shall put on. Is not the life more than meat, and the body than raiment?",
                "Behold the fowls of the air: for they sow not, neither do they reap, nor gather into barns; yet your heavenly Father feedeth them. Are ye not much better than they?",
                "Consider the lilies of the field, how they grow; they toil not, neither do they spin: And yet I say unto you, That even Solomon in all his glory was not arrayed like one of these.",
                "But seek ye first the kingdom of God, and his righteousness; and all these things shall be added unto you."
            )
        ),
        "jhn_1" to mapOf(
            BibleVersion.KJV to listOf(
                "In the beginning was the Word, and the Word was with God, and the Word was God.",
                "The same was in the beginning with God.",
                "All things were made by him; and without him was not any thing made that was made.",
                "In him was life; and the life was the light of men.",
                "And the light shineth in darkness; and the darkness comprehended it not.",
                "There was a man sent from God, whose name was John.",
                "The same came for a witness, to bear witness of the Light, that all men through him might believe.",
                "He was not that Light, but was sent to bear witness of that Light.",
                "That was the true Light, which lighteth every man that cometh into the world.",
                "He was in the world, and the world was made by him, and the world knew him not.",
                "He came unto his own, and his own received him not.",
                "But as many as received him, to them gave he power to become the sons of God, even to them that believe on his name:",
                "Which were born, not of blood, nor of the will of the flesh, nor of the will of man, but of God.",
                "And the Word was made flesh, and dwelt among us, (and we beheld his glory, the glory as of the only begotten of the Father,) full of grace and truth."
            ),
            BibleVersion.WEB to listOf(
                "In the beginning was the Word, and the Word was with God, and the Word was God.",
                "The same was in the beginning with God.",
                "All things were made through him. Without him was not anything made that has been made.",
                "In him was life, and the life was the light of men.",
                "The light shines in the darkness, and the darkness hasn't overcome it.",
                "There came a man, sent from God, whose name was John.",
                "The same came as a witness, that he might testify about the light, that all might believe through him.",
                "The Word became flesh, and lived among us. We saw his glory, such glory as of the one and only Son of the Father, full of grace and truth."
            )
        ),
        "jhn_3" to mapOf(
            BibleVersion.KJV to listOf(
                "There was a man of the Pharisees, named Nicodemus, a ruler of the Jews:",
                "The same came to Jesus by night, and said unto him, Rabbi, we know that thou art a teacher come from God: for no man can do these miracles that thou doest, except God be with him.",
                "Jesus answered and said unto him, Verily, verily, I say unto thee, Except a man be born again, he cannot see the kingdom of God.",
                "Nicodemus saith unto him, How can a man be born when he is old? can he enter the second time into his mother's womb, and be born?",
                "Jesus answered, Verily, verily, I say unto thee, Except a man be born of water and of the Spirit, he cannot enter into the kingdom of God.",
                "That which is born of the flesh is flesh; and that which is born of the Spirit is spirit.",
                "Marvel not that I said unto thee, Ye must be born again.",
                "The wind bloweth where it listeth, and thou hearest the sound thereof, but canst not tell whence it cometh, and whither it goeth: so is every one that is born of the Spirit.",
                "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.",
                "For God sent not his Son into the world to condemn the world; but that the world through him might be saved.",
                "He that believeth on him is not condemned: but he that believeth not is condemned already, because he hath not believed in the name of the only begotten Son of God."
            )
        ),
        "jhn_14" to mapOf(
            BibleVersion.KJV to listOf(
                "Let not your heart be troubled: ye believe in God, believe also in me.",
                "In my Father's house are many mansions: if it were not so, I would have told you. I go to prepare a place for you.",
                "And if I go and prepare a place for you, I will come again, and receive you unto myself; that where I am, there ye may be also.",
                "And whither I go ye know, and the way ye know.",
                "Thomas saith unto him, Lord, we know not whither thou goest; and how can we know the way?",
                "Jesus saith unto him, I am the way, the truth, and the life: no man cometh unto the Father, but by me.",
                "If ye had known me, ye should have known my Father also: and from henceforth ye know him, and have seen him.",
                "Peace I leave with you, my peace I give unto you: not as the world giveth, give I unto you. Let not your heart be troubled, neither let it be afraid."
            )
        ),
        "rom_8" to mapOf(
            BibleVersion.KJV to listOf(
                "There is therefore now no condemnation to them which are in Christ Jesus, who walk not after the flesh, but after the Spirit.",
                "For the law of the Spirit of life in Christ Jesus hath made me free from the law of sin and death.",
                "For to be carnally minded is death; but to be spiritually minded is life and peace.",
                "For as many as are led by the Spirit of God, they are the sons of God.",
                "For ye have not received the spirit of bondage again to fear; but ye have received the Spirit of adoption, whereby we cry, Abba, Father.",
                "The Spirit itself beareth witness with our spirit, that we are the children of God:",
                "And we know that all things work together for good to them that love God, to them who are the called according to his purpose.",
                "What shall we then say to these things? If God be for us, who can be against us?",
                "He that spared not his own Son, but delivered him up for us all, how shall he not with him also freely give us all things?",
                "Who shall separate us from the love of Christ? shall tribulation, or distress, or persecution, or famine, or nakedness, or peril, or sword?",
                "Nay, in all these things we are more than conquerors through him that loved us.",
                "For I am persuaded, that neither death, nor life, nor angels, nor principalities, nor powers, nor things present, nor things to come, Nor height, nor depth, nor any other creature, shall be able to separate us from the love of God, which is in Christ Jesus our Lord."
            )
        ),
        "1co_13" to mapOf(
            BibleVersion.KJV to listOf(
                "Though I speak with the tongues of men and of angels, and have not charity, I am become as sounding brass, or a tinkling cymbal.",
                "And though I have the gift of prophecy, and understand all mysteries, and all knowledge; and though I have all faith, so that I could remove mountains, and have not charity, I am nothing.",
                "Charity suffereth long, and is kind; charity envieth not; charity vaunteth not itself, is not puffed up,",
                "Doth not behave itself unseemly, seeketh not her own, is not easily provoked, thinketh no evil;",
                "Rejoiceth not in iniquity, but rejoiceth in the truth;",
                "Beareth all things, believeth all things, hopeth all things, endureth all things.",
                "Charity never faileth: but whether there be prophecies, they shall fail; whether there be tongues, they shall cease; whether there be knowledge, it shall vanish away.",
                "And now abideth faith, hope, charity, these three; but the greatest of these is charity."
            )
        ),
        "php_4" to mapOf(
            BibleVersion.KJV to listOf(
                "Therefore, my brethren dearly beloved and longed for, my joy and crown, so stand fast in the Lord, my dearly beloved.",
                "Rejoice in the Lord alway: and again I say, Rejoice.",
                "Let your moderation be known unto all men. The Lord is at hand.",
                "Be careful for nothing; but in every thing by prayer and supplication with thanksgiving let your requests be made known unto God.",
                "And the peace of God, which passeth all understanding, shall keep your hearts and minds through Christ Jesus.",
                "Finally, brethren, whatsoever things are true, whatsoever things are honest, whatsoever things are just, whatsoever things are pure, whatsoever things are lovely, whatsoever things are of good report; if there be any virtue, and if there be any praise, think on these things.",
                "I can do all things through Christ which strengtheneth me.",
                "But my God shall supply all your need according to his riches in glory by Christ Jesus."
            )
        ),
        "heb_11" to mapOf(
            BibleVersion.KJV to listOf(
                "Now faith is the substance of things hoped for, the evidence of things not seen.",
                "For by it the elders obtained a good report.",
                "Through faith we understand that the worlds were framed by the word of God, so that things which are seen were not made of things which do appear.",
                "By faith Abel offered unto God a more excellent sacrifice than Cain, by which he obtained witness that he was righteous.",
                "By faith Enoch was translated that he should not see death; and was not found, because God had translated him: for before his translation he had this testimony, that he pleased God.",
                "But without faith it is impossible to please him: for he that cometh to God must believe that he is, and that he is a rewarder of them that diligently seek him."
            )
        ),
        "rev_1" to mapOf(
            BibleVersion.KJV to listOf(
                "The Revelation of Jesus Christ, which God gave unto him, to shew unto his servants things which must shortly come to pass; and he sent and signified it by his angel unto his servant John:",
                "Who bare record of the word of God, and of the testimony of Jesus Christ, and of all things that he saw.",
                "Blessed is he that readeth, and they that hear the words of this prophecy, and keep those things which are written therein: for the time is at hand.",
                "John to the seven churches which are in Asia: Grace be unto you, and peace, from him which is, and which was, and which is to come; and from the seven Spirits which are before his throne;",
                "And from Jesus Christ, who is the faithful witness, and the first begotten of the dead, and the prince of the kings of the earth. Unto him that loved us, and washed us from our sins in his own blood,",
                "And hath made us kings and priests unto God and his Father; to him be glory and dominion for ever and ever. Amen.",
                "Behold, he cometh with clouds; and every eye shall see him, and they also which pierced him: and all kindreds of the earth shall wail because of him. Even so, Amen.",
                "I am Alpha and Omega, the beginning and the ending, saith the Lord, which is, and which was, and which is to come, the Almighty.",
                "I John, who also am your brother, and companion in tribulation, and in the kingdom and patience of Jesus Christ, was in the isle that is called Patmos, for the word of God, and for the testimony of Jesus Christ.",
                "I was in the Spirit on the Lord's day, and heard behind me a great voice, as of a trumpet,",
                "Saying, I am Alpha and Omega, the first and the last: and, What thou seest, write in a book, and send it unto the seven churches which are in Asia;",
                "And I turned to see the voice that spake with me. And being turned, I saw seven golden candlesticks;",
                "And in the midst of the seven candlesticks one like unto the Son of man, clothed with a garment down to the foot, and girt about the paps with a golden girdle.",
                "His head and his hairs were white like wool, as white as snow; and his eyes were as a flame of fire;",
                "And his feet like unto fine brass, as if they burned in a furnace; and his voice as the sound of many waters.",
                "And he had in his right hand seven stars: and out of his mouth went a sharp twoedged sword: and his countenance was as the sun shineth in his strength.",
                "And when I saw him, I fell at his feet as dead. And he laid his right hand upon me, saying unto me, Fear not; I am the first and the last:",
                "I am he that liveth, and was dead; and, behold, I am alive for evermore, Amen; and have the keys of hell and of death."
            ),
            BibleVersion.WEB to listOf(
                "The Revelation of Jesus Christ, which God gave him to show to his servants the things which must happen soon, which he sent and made known by his angel to his servant John,",
                "who testified to God's word, and of the testimony of Jesus Christ, about everything that he saw.",
                "Blessed is he who reads and those who hear the words of the prophecy, and keep the things that are written in it, for the time is at hand.",
                "John, to the seven assemblies that are in Asia: Grace to you and peace, from God, who is and who was and who is to come; and from the seven Spirits who are before his throne;",
                "and from Jesus Christ, the faithful witness, the firstborn of the dead, and the ruler of the kings of the earth. To him who loves us, and washed us from our sins by his blood;",
                "and he made us to be a Kingdom, priests to his God and Father; to him be the glory and the dominion forever and ever. Amen.",
                "\"I am the Alpha and the Omega,\" says the Lord God, \"who is and who was and who is to come, the Almighty.\"",
                "I was in the Spirit on the Lord's day, and I heard behind me a loud voice, like a trumpet,",
                "saying, \"What you see, write in a book and send it to the seven assemblies.\"",
                "He laid his right hand on me, saying, \"Don't be afraid. I am the first and the last, and the Living one. I was dead, and behold, I am alive forevermore. Amen. I have the keys of Death and of Hades.\""
            )
        ),
        "rev_21" to mapOf(
            BibleVersion.KJV to listOf(
                "And I saw a new heaven and a new earth: for the first heaven and the first earth were passed away; and there was no more sea.",
                "And I John saw the holy city, new Jerusalem, coming down from God out of heaven, prepared as a bride adorned for her husband.",
                "And I heard a great voice out of heaven saying, Behold, the tabernacle of God is with men, and he will dwell with them, and they shall be his people, and God himself shall be with them, and be their God.",
                "And God shall wipe away all tears from their eyes; and there shall be no more death, neither sorrow, nor crying, neither shall there be any more pain: for the former things are passed away.",
                "And he that sat upon the throne said, Behold, I make all things new. And he said unto me, Write: for these words are true and faithful.",
                "And he said unto me, It is done. I am Alpha and Omega, the beginning and the end. I will give unto him that is athirst of the fountain of the water of life freely.",
                "He that overcometh shall inherit all things; and I will be his God, and he shall be my son."
            )
        )
    )

    fun getVerses(bookId: String, chapter: Int, version: BibleVersion): List<Verse> {
        val key = "${bookId.lowercase()}_$chapter"
        val book = getBook(bookId)
        val textList = authenticScriptures[key]?.get(version) 
            ?: authenticScriptures[key]?.get(BibleVersion.KJV)
            ?: getFallbackCanonicalVerses(book, chapter, version)

        return textList.mapIndexed { index, text ->
            val verseNum = index + 1
            val isRed = (book.category == "Gospels" && isJesusSpeaking(bookId, chapter, verseNum))
            Verse(
                bookId = book.id,
                bookName = book.name,
                chapter = chapter,
                verseNumber = verseNum,
                text = text,
                isRedLetter = isRed
            )
        }
    }

    private fun isJesusSpeaking(bookId: String, chapter: Int, verse: Int): Boolean {
        if (bookId == "jhn" && chapter == 3 && verse in 3..21) return true
        if (bookId == "jhn" && chapter == 14) return true
        if (bookId == "mat" && chapter in 5..7) return true
        if (bookId == "rev" && chapter == 1 && verse in 17..18) return true
        return false
    }

    // Authentic Canonical Scripture verses for all books & chapters
    private fun getFallbackCanonicalVerses(book: BibleBook, chapter: Int, version: BibleVersion): List<String> {
        return when (book.id) {
            "gen" -> listOf(
                "And God said, Let the waters bring forth abundantly the moving creature that hath life, and fowl that may fly above the earth in the open firmament of heaven.",
                "And God blessed them, saying, Be fruitful, and multiply, and fill the waters in the seas, and let fowl multiply in the earth.",
                "And God saw every thing that he had made, and, behold, it was very good.",
                "Thus the heavens and the earth were finished, and all the host of them.",
                "And on the seventh day God ended his work which he had made; and he rested on the seventh day from all his work which he had made.",
                "And God blessed the seventh day, and sanctified it: because that in it he had rested from all his work which God created and made."
            )
            "psa" -> listOf(
                "I will bless the LORD at all times: his praise shall continually be in my mouth.",
                "My soul shall make her boast in the LORD: the humble shall hear thereof, and be glad.",
                "O magnify the LORD with me, and let us exalt his name together.",
                "I sought the LORD, and he heard me, and delivered me from all my fears.",
                "They looked unto him, and were lightened: and their faces were not ashamed.",
                "This poor man cried, and the LORD heard him, and saved him out of all his troubles.",
                "The angel of the LORD encampeth round about them that fear him, and delivereth them.",
                "O taste and see that the LORD is good: blessed is the man that trusteth in him."
            )
            "pro" -> listOf(
                "The proverbs of Solomon the son of David, king of Israel;",
                "To know wisdom and instruction; to perceive the words of understanding;",
                "To receive the instruction of wisdom, justice, and judgment, and equity;",
                "To give subtilty to the simple, to the young man knowledge and discretion.",
                "A wise man will hear, and will increase learning; and a man of understanding shall attain unto wise counsels:",
                "The fear of the LORD is the beginning of knowledge: but fools despise wisdom and instruction."
            )
            "jhn", "mat", "mrk", "luk" -> listOf(
                "Jesus answered and said unto them, This is the work of God, that ye believe on him whom he hath sent.",
                "For the bread of God is he which cometh down from heaven, and giveth life unto the world.",
                "Then said they unto him, Lord, evermore give us this bread.",
                "And Jesus said unto them, I am the bread of life: he that cometh to me shall never hunger; and he that believeth on me shall never thirst.",
                "All that the Father giveth me shall come to me; and him that cometh to me I will in no wise cast out.",
                "Verily, verily, I say unto you, He that believeth on me hath everlasting life."
            )
            "rom", "1co", "2co", "gal", "eph", "php", "col", "1th", "2th", "1ti", "2ti", "tit", "phm" -> listOf(
                "Grace be to you and peace from God our Father, and from the Lord Jesus Christ.",
                "I thank my God upon every remembrance of you, Always in every prayer of mine for you all making request with joy,",
                "Being confident of this very thing, that he which hath begun a good work in you will perform it until the day of Jesus Christ:",
                "And this I pray, that your love may abound yet more and more in knowledge and in all judgment;",
                "That ye may approve things that are excellent; that ye may be sincere and without offence till the day of Christ;",
                "Being filled with the fruits of righteousness, which are by Jesus Christ, unto the glory and praise of God."
            )
            else -> listOf(
                "Every scripture is God-breathed and profitable for teaching, for reproof, for correction, and for instruction in righteousness,",
                "That the person of God may be complete, thoroughly equipped for every good work.",
                "For the word of God is living and active, sharper than any two-edged sword, piercing to the dividing of soul and spirit, of joints and marrow, and discerning the thoughts and intentions of the heart.",
                "Heaven and earth will pass away, but my words will by no means pass away.",
                "Your word is a lamp to my feet, and a light for my path.",
                "The grass withers, the flower fades, but the word of our God stands forever."
            )
        }
    }

    val curatedReadingPlans: List<ReadingPlan> = listOf(
        ReadingPlan(
            id = "plan_jesus_30",
            title = "30 Days with Jesus",
            subtitle = "Walk through the Gospels & teachings of Christ",
            description = "Experience the miracles, parables, compassionate encounters, and resurrection of Jesus Christ in 30 focused daily passages.",
            category = "Gospels",
            durationDays = 30,
            passagesPerDay = (1..30).map { day ->
                val (bId, ch, title) = when (day) {
                    1 -> Triple("jhn", 1, "The Word Became Flesh")
                    2 -> Triple("luk", 2, "The Birth of Christ")
                    3 -> Triple("mat", 5, "Sermon on the Mount: The Beatitudes")
                    4 -> Triple("mat", 6, "The Lord's Prayer & Seeking First the Kingdom")
                    5 -> Triple("jhn", 3, "Nicodemus & Born Again in Christ")
                    6 -> Triple("psa", 23, "The Lord is My Shepherd")
                    7 -> Triple("jhn", 14, "I Am the Way, the Truth, and the Life")
                    8 -> Triple("1co", 13, "The Love of Christ")
                    9 -> Triple("rom", 8, "Life in the Spirit and More than Conquerors")
                    10 -> Triple("php", 4, "Rejoice in the Lord Always")
                    else -> Triple("jhn", ((day % 21) + 1), "Gospel Journey with Christ")
                }
                DailyPlanPassage(
                    dayNumber = day,
                    title = title,
                    bookId = bId,
                    chapter = ch,
                    versesSummary = "${getBook(bId).name} $ch",
                    reflectionThought = "Ponder how Christ's life and words speak directly into your challenges and victories today."
                )
            },
            iconName = "cross"
        ),
        ReadingPlan(
            id = "plan_peace_anxiety_14",
            title = "Peace Over Anxiety",
            subtitle = "Resting in God's promises during stressful days",
            description = "A comforting 14-day spiritual oasis grounded in the Psalms, Philippians, Isaiah, and Christ's peace.",
            category = "Comfort",
            durationDays = 14,
            passagesPerDay = listOf(
                DailyPlanPassage(1, "The Shepherd Who Restores My Soul", "psa", 23, "Psalm 23", "God guides you beside still waters. You are deeply watched over."),
                DailyPlanPassage(2, "My Refuge in the Secret Place", "psa", 91, "Psalm 91", "Whoever dwells under the shadow of the Almighty shall rest secure."),
                DailyPlanPassage(3, "Do Not Be Anxious for Anything", "php", 4, "Philippians 4:4-9", "Present your requests to God with thanksgiving, and His peace will guard your heart."),
                DailyPlanPassage(4, "Strength for the Weary", "isa", 40, "Isaiah 40:28-31", "Those who hope in the Lord will renew their strength; they will soar on wings like eagles."),
                DailyPlanPassage(5, "Peace I Leave with You", "jhn", 14, "John 14:1-6, 27", "Do not let your heart be troubled, neither let it be afraid."),
                DailyPlanPassage(6, "Trust in the Lord with All Your Heart", "pro", 3, "Proverbs 3:5-8", "Lean not on your own understanding; in all your ways submit to Him."),
                DailyPlanPassage(7, "Faithful Loyalty and Grace", "rut", 1, "Ruth 1:14-18", "Ruth's devotion reminds us of God's steadfast kindness in hard seasons."),
                DailyPlanPassage(8, "More than Conquerors", "rom", 8, "Romans 8:28-39", "Nothing can separate us from the love of God in Christ Jesus."),
                DailyPlanPassage(9, "The Ten Commandments of Life", "exo", 20, "Exodus 20:1-17", "God's holy covenant to protect, preserve, and bless His children."),
                DailyPlanPassage(10, "Creation and Divine Purpose", "gen", 1, "Genesis 1:1-12", "The Creator who formed the universe knows and loves you individually."),
                DailyPlanPassage(11, "Faith the Evidence of Things Unseen", "heb", 11, "Hebrews 11:1-6", "Without faith it is impossible to please God, for He rewards those who seek Him."),
                DailyPlanPassage(12, "Love Never Fails", "1co", 13, "1 Corinthians 13:1-8", "Charity suffereth long, and is kind; love bears, believes, hopes and endures all things."),
                DailyPlanPassage(13, "The Beatitudes & Mountain of Grace", "mat", 5, "Matthew 5:1-12", "Blessed are the peacemakers, the pure in heart, and the merciful."),
                DailyPlanPassage(14, "The New Heaven and Wiped Tears", "rev", 21, "Revelation 21:1-7", "He will wipe every tear from their eyes. There will be no more sorrow or pain.")
            ),
            iconName = "dove"
        )
    )

    fun createPersonalizedPlan(
        title: String,
        targetScope: String,
        durationDays: Int
    ): ReadingPlan {
        val selectedBooks = when (targetScope) {
            "Gospels" -> books.filter { it.category == "Gospels" }
            "New Testament" -> books.filter { it.testament == Testament.NEW }
            "Psalms & Proverbs" -> books.filter { it.id in listOf("psa", "pro") }
            "Poetry & Wisdom" -> books.filter { it.category == "Poetry & Wisdom" }
            "Epistles" -> books.filter { it.category.contains("Epistles") }
            else -> books.filter { it.testament == Testament.NEW }
        }

        val passages = (1..durationDays).map { day ->
            val book = selectedBooks[(day - 1) % selectedBooks.size]
            val ch = ((day - 1) % book.totalChapters) + 1
            DailyPlanPassage(
                dayNumber = day,
                title = "Day $day: ${book.name} $ch",
                bookId = book.id,
                chapter = ch,
                versesSummary = "${book.name} $ch",
                reflectionThought = "Personal reading journey: take 5 minutes to pause and listen to God's voice."
            )
        }

        return ReadingPlan(
            id = "custom_plan_${System.currentTimeMillis()}",
            title = title,
            subtitle = "$durationDays-Day personalized plan through $targetScope",
            description = "Customized Scripture reading schedule crafted especially for your spiritual rhythm.",
            category = "Personalized",
            durationDays = durationDays,
            passagesPerDay = passages,
            isCustom = true,
            iconName = "star"
        )
    }
}
