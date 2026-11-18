package com.example.data.repository

import com.example.data.model.BhaktiBodhTopic
import com.example.data.model.ContentItem
import com.example.data.model.KnowledgeCategory
import com.example.data.model.MediaType
import com.example.data.model.PhotoAlbum
import com.example.data.model.PhotoItem
import com.example.data.model.ScriptureEvidence
import com.example.data.model.SrishtiRachnaSection

object InitialData {

    val srishtiRachnaSections = listOf(
        SrishtiRachnaSection(
            id = "sr_1",
            stepNumber = 1,
            title = "सृष्टि रचना का मूल परिचय",
            subtitle = "सर्वप्रथम केवल पूर्ण परमात्मा कबीर साहेब ही विद्यमान थे",
            detailedText = """
                सर्वप्रथम सृष्टि से पूर्व केवल पूर्ण परमात्मा कबीर साहेब (कविर्देव) ही अपने वास्तविक अमरलोक (सतलोक) में अनामी पुरुष रूप में विद्यमान थे। उस समय न तो काल ब्रह्म था, न दुर्गा, न ब्रह्मा, न विष्णु, न शिव, न यह 21 ब्रह्मांड और न ही 84 लाख योनियाँ।
                
                परमात्मा कबीर साहेब ने अपने शरीर से शब्द शक्ति द्वारा लोकों की रचना की। अनामी लोक से अगम लोक, अगम लोक से अलख लोक तथा अलख लोक से सतलोक (अमरलोक/सच्चखंड) का प्राकट्य हुआ।
                
                सतलोक में परमात्मा ने 16 पुत्रों को शब्द से उत्पन्न किया, जिनमें प्रथम पुत्र 'कूर्म' और कनिष्ठ पुत्र 'ज्योति निरंजन' (काल ब्रह्म / क्षर पुरुष) था।
            """.trimIndent(),
            scripturalReferences = listOf(
                "कबीर सागर - सृष्टि रचना अध्याय",
                "ऋग्वेद मण्डल 10 सूक्त 90 मन्त्र 1-5",
                "श्रीमद्भगवद्गीता अध्याय 15 श्लोक 1-4"
            ),
            videoUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E",
            keyPoints = listOf(
                "अनामी पुरुष ही सर्वोच्च सत्ता हैं।",
                "सतलोक अमर, अविनाशी एवं अखंड है जहाँ कभी जन्म-मृत्यु नहीं होती।",
                "आत्माएं मूल रूप से सतलोक की हंस थीं जो काल के जाल में फँस गईं।"
            )
        ),
        SrishtiRachnaSection(
            id = "sr_2",
            stepNumber = 2,
            title = "सतलोक (अमरलोक) का भव्य वर्णन",
            subtitle = "दिव्य मानसरोवर, अखण्ड प्रकाश और सुखों का सागर",
            detailedText = """
                सतलोक (सचखंड) में एक-एक हंस (आत्मा) के शरीर का प्रकाश 16 सूर्यों के समान है और परमपिता कबीर साहेब के एक रोम कूप का प्रकाश करोड़ सूर्यों तथा करोड़ चंद्रमाओं से भी अधिक दिव्य है।
                
                वहां दिव्य मानसरोवर है जिसका जल अमृत है। वहां कल्पवृक्ष, दिव्य महल और कभी न मुरझाने वाले सुगंधित पुष्प हैं। वहां बुढ़ापा, रोग, मृत्यु, चिंता, लोभ, काम, क्रोध और काल का कोई अस्तित्व नहीं है।
                
                कबीर साहेब जी की वाणी:
                'शंखों पदमों दीप हैं, सब सुखों का सागर।
                हंसा तहां किलोल करें, जहं पूर्ण परमात्मा कबीर।।'
            """.trimIndent(),
            scripturalReferences = listOf(
                "कबीर सागर - अमरलोक बोध",
                "ऋग्वेद मण्डल 9 सूक्त 96 मन्त्र 17-20",
                "सामवेद संख्या 1400"
            ),
            videoUrl = "https://www.youtube.com/watch?v=W-rC6bZ6z5I",
            keyPoints = listOf(
                "16 शंख कोस में फैला हुआ अमर क्षेत्र।",
                "चिंता-शोक रहित अमृतमय आनंद की भूमि।",
                "समस्त आत्माओं का वास्तविक पैतृक घर।"
            )
        ),
        SrishtiRachnaSection(
            id = "sr_3",
            stepNumber = 3,
            title = "ज्योति निरंजन (काल ब्रह्म) की उत्पत्ति व तप",
            subtitle = "काल ब्रह्म को 21 ब्रह्मांड और दुर्गा (अष्टांगी) का मिलना",
            detailedText = """
                ज्योति निरंजन (क्षर पुरुष) ने सतलोक में 70 युग तक एक पैर पर खड़े होकर कठिन तप किया। परमात्मा कबीर साहेब ने प्रसन्न होकर उसे 21 ब्रह्मांड प्रदान किए।
                
                इसके बाद ज्योति निरंजन के मन में स्वतंत्र सत्ता की इच्छा हुई और उसने अन्य आत्माओं को अपने वश में करने का विचार किया। परमात्मा ने अष्टांगी (प्रकृति/दुर्गा) की रचना की। ज्योति निरंजन ने दुर्गा के साथ छल किया, जिसके कारण परमात्मा कबीर साहेब ने दोनों को 21 ब्रह्मांड सहित सतलोक से निष्कासित कर दिया।
                
                यह सम्पूर्ण 21 ब्रह्मांड काल का क्षेत्र है जहाँ जन्म-मरण का चक्र चलता है।
            """.trimIndent(),
            scripturalReferences = listOf(
                "कबीर सागर - ज्ञान सागर",
                "श्रीमद्देवीभागवत महापुराण",
                "श्रीमद्भगवद्गीता अध्याय 14 श्लोक 3-5"
            ),
            videoUrl = "https://www.youtube.com/watch?v=kYQG2L0hGZs",
            keyPoints = listOf(
                "21 ब्रह्मांडों का स्वामी काल ब्रह्म (क्षर पुरुष) है।",
                "दुर्गा (प्रकृति) और काल ब्रह्म के संयोग से ब्रह्मा, विष्णु, महेश उत्पन्न हुए।",
                "हम सब आत्माएं काल के जाल में बंधनग्रस्त हैं।"
            )
        ),
        SrishtiRachnaSection(
            id = "sr_4",
            stepNumber = 4,
            title = "ब्रह्मा, विष्णु एवं महेश की उत्पत्ति व कार्य विभाजन",
            subtitle = "रजगुण ब्रह्मा, सतगुण विष्णु, तमगुण शिव जी का रहस्य",
            detailedText = """
                ज्योति निरंजन (काल) और दुर्गा (प्रकृति) के संयोग से तीन पुत्र उत्पन्न हुए:
                1. ब्रह्मा जी - रजगुण (सृजन कर्ता)
                2. विष्णु जी - सतगुण (पालन कर्ता)
                3. शिव जी - तमगुण (संहार कर्ता)
                
                काल ब्रह्म ने इन तीनों को अचेत करके अलग-अलग लोकों में स्थापित किया और इनकी स्मृति गुप्त रखी ताकि ये अपने वास्तविक पिता काल ब्रह्म को न जान सकें।
                
                श्रीमद्देवीभागवत पुराण (तीसरे स्कंध, अध्याय 5) में स्वयं ब्रह्मा, विष्णु और महेश स्वीकार करते हैं कि हम नाशवान हैं और हमारा जन्म माता दुर्गा और पिता द्वारा हुआ है।
            """.trimIndent(),
            scripturalReferences = listOf(
                "श्रीमद्देवीभागवत महापुराण - स्कंध 3 अध्याय 5 श्लोक 12",
                "शिव महापुराण - रुद्र संहिता",
                "श्रीमद्भगवद्गीता अध्याय 7 श्लोक 12-15"
            ),
            videoUrl = "https://www.youtube.com/watch?v=4yJc9q-2a7w",
            keyPoints = listOf(
                "तीनों देव तीन गुणों के प्रतीक हैं।",
                "तीनों देवों की आयु सीमित है और वे जन्म-मरण में हैं।",
                "परम मोक्ष केवल पूर्ण परमात्मा कबीर साहेब की सतभक्ति से ही संभव है।"
            )
        )
    )

    val scriptureEvidences = listOf(
        // वेदों में प्रमाण
        ScriptureEvidence(
            id = "sc_veda_1",
            subcategory = "वेदों में प्रमाण",
            scriptureName = "ऋग्वेद",
            chapterVerse = "मण्डल 9 सूक्त 96 मन्त्र 17",
            originalVerse = "शिशुं जज्ञानं हर्यतं मृजन्ति शुम्भन्ति विप्रं धियो मणीषा। शिशुं न विप्रा मतिभी रिहन्ति कविर्गीर्भिः काव्येना कवीनाम्।।",
            hindiExplanation = "इस मन्त्र में स्पष्ट लिखा है कि पूर्ण परमात्मा अमरलोक से शिशु रूप धारण करके आता है, कुंवारी गायों के दूध से उसका पालन होता है और उसका वास्तविक अमर नाम 'कविर्देव' (कबीर साहेब) है। वह तत्वज्ञान को कवियों की भांति कविताओं और वाणियों के रूप में उच्चारित करता है।",
            videoUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E"
        ),
        ScriptureEvidence(
            id = "sc_veda_2",
            subcategory = "वेदों में प्रमाण",
            scriptureName = "यजुर्वेद",
            chapterVerse = "अध्याय 29 मन्त्र 25",
            originalVerse = "समिद्धो अद्य मनुषो दुरोणे देवो देवान्यजसि जातवेदः। आ च वह मित्रमहश्चिकित्वान्त्वं दूतः कविरसि प्रचेताः।।",
            hindiExplanation = "इस मन्त्र में सर्वशक्तिमान परमात्मा का नाम स्पष्ट रूप से 'कविर्' (कबीर) कहा गया है। वह सर्वज्ञानी, दयालु और जीवों को सत्य सन्देश देने वाला परम प्रभु है।",
            videoUrl = "https://www.youtube.com/watch?v=W-rC6bZ6z5I"
        ),
        ScriptureEvidence(
            id = "sc_veda_3",
            subcategory = "वेदों में प्रमाण",
            scriptureName = "ऋग्वेद",
            chapterVerse = "मण्डल 1 सूक्त 31 मन्त्र 17",
            originalVerse = "कविर्अग्निर्उप स्तुहि...",
            hindiExplanation = "ऋग्वेद में परमात्मा को 'कविर्अग्नि' कहा गया है, जिसका अर्थ है पापों को भस्म करने वाला अविनाशी कबीर परमेश्वर।",
            videoUrl = "https://www.youtube.com/watch?v=kYQG2L0hGZs"
        ),

        // श्रीमद्भगवद्गीता में प्रमाण
        ScriptureEvidence(
            id = "sc_gita_1",
            subcategory = "श्रीमद्भगवद्गीता में प्रमाण",
            scriptureName = "श्रीमद्भगवद्गीता",
            chapterVerse = "अध्याय 15 श्लोक 1-4",
            originalVerse = "ऊर्ध्वमूलमधःशाखमश्वत्थं प्राहुरव्ययम्। छन्दांसि यस्य पर्णानि यस्तं वेद स वेदवित्।।",
            hindiExplanation = "गीता जी में संसार रूपी उल्टे लटके वृक्ष का वर्णन है। ऊपर को मूल (परमात्मा कबीर साहेब), नीचे को शाखाएं (ब्रह्मा, विष्णु, महेश)। जो इस उल्टे संसार रूपी वृक्ष के सभी भागों को तत्व से जानता है, वही पूर्ण तत्वदर्शी संत है।",
            videoUrl = "https://www.youtube.com/watch?v=4yJc9q-2a7w"
        ),
        ScriptureEvidence(
            id = "sc_gita_2",
            subcategory = "श्रीमद्भगवद्गीता में प्रमाण",
            scriptureName = "श्रीमद्भगवद्गीता",
            chapterVerse = "अध्याय 18 श्लोक 62",
            originalVerse = "तमेव शरणं गच्छ सर्वभावेन भारत। तत्प्रसादात्परां शान्तिं स्थानं प्राप्स्यसि शाश्वतम्।।",
            hindiExplanation = "गीता ज्ञान दाता काल ब्रह्म अर्जुन से कहता है: हे भारत! तू सब भाव से उस दूसरे परमेश्वर (परम अक्षर ब्रह्म) की शरण में जा, जिसकी कृपा से तू परम शांति तथा सनातन अविनाशी परम पद (सतलोक) को प्राप्त करेगा।",
            videoUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E"
        ),
        ScriptureEvidence(
            id = "sc_gita_3",
            subcategory = "श्रीमद्भगवद्गीता में प्रमाण",
            scriptureName = "श्रीमद्भगवद्गीता",
            chapterVerse = "अध्याय 4 श्लोक 34",
            originalVerse = "तद्विद्धि प्रणिपातेन परिप्रश्नेन सेवया। उपदेक्ष्यन्ति ते ज्ञानं ज्ञानिनस्तत्त्वदर्शिनः।।",
            hindiExplanation = "उस पूर्ण मोक्ष दायक ज्ञान को तत्वदर्शी संतों के पास जाकर समझ। उन्हें दण्डवत् प्रणाम करने से, निष्कपट प्रश्न करने से और उनकी सेवा करने से वे तत्वदर्शी ज्ञानी महात्मा तुझे सत्य ज्ञान का उपदेश करेंगे।",
            videoUrl = "https://www.youtube.com/watch?v=W-rC6bZ6z5I"
        ),

        // पुराणों में प्रमाण
        ScriptureEvidence(
            id = "sc_puran_1",
            subcategory = "पुराणों में प्रमाण",
            scriptureName = "श्रीमद्देवीभागवत महापुराण",
            chapterVerse = "तीसरा स्कंध, अध्याय 5 श्लोक 12",
            originalVerse = "विष्णुः उवाच: अहम् ईश्वरो न, त्वमपि न शंभुः...",
            hindiExplanation = "भगवान विष्णु माता दुर्गा से कहते हैं कि हे माता! न तो मैं स्वतंत्र ईश्वर हूँ, न ब्रह्मा जी और न ही शंकर जी। हम सब आपके अधीन हैं और हमारे जन्म और मृत्यु होते हैं। इससे स्पष्ट है कि त्रिदेव नाशवान हैं।",
            videoUrl = "https://www.youtube.com/watch?v=kYQG2L0hGZs"
        ),
        ScriptureEvidence(
            id = "sc_puran_2",
            subcategory = "पुराणों में प्रमाण",
            scriptureName = "शिव महापुराण",
            chapterVerse = "रुद्र संहिता, सृष्टि खण्ड",
            originalVerse = "सदाशिव एवं प्रकृति दुर्गा द्वारा त्रिदेवों का प्राकट्य",
            hindiExplanation = "सदाशिव (काल ब्रह्म) और आद्या शक्ति प्रकृति ने मिलकर ब्रह्मा, विष्णु और महेश को उत्पन्न किया।",
            videoUrl = "https://www.youtube.com/watch?v=4yJc9q-2a7w"
        ),

        // अन्य पवित्र शास्त्रों में प्रमाण
        ScriptureEvidence(
            id = "sc_other_1",
            subcategory = "अन्य पवित्र शास्त्रों में प्रमाण",
            scriptureName = "श्री गुरु ग्रंथ साहिब",
            chapterVerse = "राग आसा, महला १, पृष्ठ ७२१",
            originalVerse = "हक्का कबीर करीम तू बेऐब परवरदिगार। नानक बुगोयद जनु तुरा तेरे चाकरां पाखाक।।",
            hindiExplanation = "आदरणीय श्री गुरु नानक देव जी महाराज स्पष्ट गवाही देते हैं कि हे कबीर साहेब! आप ही सच्चे परमात्मा (हक्का), दयालु (करीम), दोषरहित (बेऐब) और सृष्टि के पालनहार हैं। नानक आपके दासों का दास है।",
            videoUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E"
        ),
        ScriptureEvidence(
            id = "sc_other_2",
            subcategory = "अन्य पवित्र शास्त्रों में प्रमाण",
            scriptureName = "पवित्र कुरान शरीफ",
            chapterVerse = "सूरह अल-फुरकान 25 आयत 52-59",
            originalVerse = "तवक्कल अलल हय्यिल्लजी ला यमूतु... अल्लजी खलकस्समावाति वल अर्ज़... अर्रहमानु फस्अल बिही खबीरन।",
            hindiExplanation = "कुरान मजीद में हजरत मुहम्मद साहिब को आदेश है कि उस जीवित परमात्मा पर भरोसा रखो जो कभी नहीं मरता। जिसने 6 दिन में आकाश और पृथ्वी की रचना की और सातवें दिन तख्त पर जा विराजा। वह दयालु परमात्मा 'कबीरन' (कबीर) है, उसका वास्तविक ज्ञान किसी बाखबर (तत्वदर्शी संत) से पूछो।",
            videoUrl = "https://www.youtube.com/watch?v=W-rC6bZ6z5I"
        ),
        ScriptureEvidence(
            id = "sc_other_3",
            subcategory = "अन्य पवित्र शास्त्रों में प्रमाण",
            scriptureName = "पवित्र बाईबल",
            chapterVerse = "उत्पत्ति (Genesis) अध्याय 1 आयत 26-27",
            originalVerse = "God created man in his own image...",
            hindiExplanation = "बाईबल प्रमाणित करती है कि परमात्मा निराकार नहीं अपितु नर-आकार (साकार) है, जिसने मनुष्य को अपने ही स्वरूप जैसा बनाया और वह परमपिता कबीर साहेब हैं।",
            videoUrl = "https://www.youtube.com/watch?v=kYQG2L0hGZs"
        ),

        // कबीर साहिब परमात्मा के प्रमाण
        ScriptureEvidence(
            id = "sc_kabir_1",
            subcategory = "कबीर साहिब परमात्मा के प्रमाण",
            scriptureName = "कबीर वाणी / साखी",
            chapterVerse = "अमरवाणी",
            originalVerse = "हम ही अलख अलह हैं, कुतुब मझाक हमारा।\nकहें कबीर सुनो भाई साधो, हम ही सिरजनहारा।।",
            hindiExplanation = "कबीर साहेब जी ने स्वयं काशी की पावन धरा पर 120 वर्ष लीला करते हुए तत्वज्ञान प्रकट किया और बताया कि वे ही सृष्टि के वास्तविक जनक और मुक्तिदाता हैं।",
            videoUrl = "https://www.youtube.com/watch?v=4yJc9q-2a7w"
        )
    )

    val bhaktiBodhTopics = listOf(
        BhaktiBodhTopic(
            id = "bb_1",
            title = "असुर निकन्दन रमैणी",
            hindiTitle = "असुर निकन्दन रमैणी (Asur Nikandan Ramaini)",
            timing = "नित्य पाठ - प्रातः व संध्या",
            fullText = """
                सत सुकृत कहि सिमरहुं नाऊ, भवसागर में बहुरि न आऊ।
                आदि नाम ओंकार ध्याऊ, ता पीछे फिर नाम सुणाऊ।।
                
                सुर नर मुनि जन सब भरमाये, गोरख दत सब अटकाये।
                शंभु सनक सहस्र मुख गावै, सुर मुनि अन्त न पावै।।
                
                कहहिं कबीर सुनो भाई साधो, यह सब काल का पसारा।
                अमर लोक को चलो हंसा, जहाँ साहिब का दरबारा।।
            """.trimIndent(),
            meaning = "यह रमैणी समस्त आसुरी वृत्तियों, काल के भ्रम और अज्ञान का नाश करने वाली अमृत वाणी है। इसका नित्य स्मरण करने से जीव के कर्म बंधन कटते हैं और मोक्ष मार्ग प्रशस्त होता है।",
            audioUrl = "https://example.com/audio/asur_nikandan.mp3",
            videoUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E",
            duration = "12:45"
        ),
        BhaktiBodhTopic(
            id = "bb_2",
            title = "संध्या आरती",
            hindiTitle = "संध्या आरती (Sandhya Aarti)",
            timing = "संध्या काल",
            fullText = """
                संध्या आरती सतगुरु दीन दयाला, भवसागर से तारनहारा।
                ज्योति स्वरूप पूर्ण अविनाशी, अमर लोक के वासी।।
                
                कोटि सूर्य जाके रोम विराजे, शंख चक्र गदा पद्म साजे।
                आरती करूँ प्रेम रस भीनी, कबीर साहिब दया तुम कीनी।।
            """.trimIndent(),
            meaning = "संध्या समय परमात्मा कबीर साहेब तथा सतगुरु देव जी की स्तुति व आरती, जिससे अंतःकरण शुद्ध होता है और गृह में शांति का वास होता है।",
            audioUrl = "https://example.com/audio/sandhya_aarti.mp3",
            videoUrl = "https://www.youtube.com/watch?v=W-rC6bZ6z5I",
            duration = "18:20"
        ),
        BhaktiBodhTopic(
            id = "bb_3",
            title = "मंगलाचरण",
            hindiTitle = "मंगलाचरण (Mangalacharan)",
            timing = "सत्संग व पाठ के आरंभ में",
            fullText = """
                कबीर गुरु बिन काहू न पाईया, सांच कहूँ समुझाय।
                चौरासी लख जोनि में, भरमि भरमि दुख पाय।।
                
                सतगुरु पुरुष कबीर हैं, आदि अंत के मीत।
                जाके सिमरन से मिटे, भवसागर का भीत।।
            """.trimIndent(),
            meaning = "सत्संग, पूजा अथवा किसी भी शुभ कार्य के आरंभ में मंगलमय वातावरण निर्मित करने हेतु गुरु स्तुति।",
            audioUrl = "https://example.com/audio/mangalacharan.mp3",
            videoUrl = "https://www.youtube.com/watch?v=kYQG2L0hGZs",
            duration = "08:15"
        ),
        BhaktiBodhTopic(
            id = "bb_4",
            title = "भोग आरती",
            hindiTitle = "प्रसाद भोग आरती (Bhog Aarti)",
            timing = "भोजन व प्रसाद अर्पण समय",
            fullText = """
                साहिब तुमको भोग लगाऊँ, प्रेम प्रीति से शीश नवाऊँ।
                अमृत भोजन पावन कीजै, अपनी दया की वर्षा कीजै।।
            """.trimIndent(),
            meaning = "भोजन को सात्विक व अमृतमय बनाने हेतु पूर्ण परमात्मा को भोग समर्पण की पावन विधि।",
            audioUrl = "https://example.com/audio/bhog_aarti.mp3",
            videoUrl = "https://www.youtube.com/watch?v=4yJc9q-2a7w",
            duration = "05:30"
        )
    )

    val videos = listOf(
        ContentItem(
            id = "v_1",
            title = "सृष्टि की रचना कैसे हुई? (Creation of Nature)",
            description = "संत रामपाल जी महाराज द्वारा पवित्र सद्ग्रंथों के आधार पर सृष्टि रचना का अकाट्य आध्यात्मिक विश्लेषण।",
            type = MediaType.VIDEO,
            category = KnowledgeCategory.SRISHTI_RACHNA,
            playlist = "Srishti Rachna",
            thumbnailUrl = "https://img.youtube.com/vi/0k5j-eLgX2E/hqdefault.jpg",
            mediaUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E",
            duration = "48:15",
            isFeatured = true,
            viewsCount = 54200
        ),
        ContentItem(
            id = "v_2",
            title = "वेदों में कबीर परमेश्वर के साक्षात प्रमाण",
            description = "ऋग्वेद, यजुर्वेद, सामवेद और अथर्ववेद में पूर्ण परमात्मा 'कविर्देव' का विशद प्रमाण।",
            type = MediaType.VIDEO,
            category = KnowledgeCategory.SHASTRARTH,
            playlist = "Shastrarth",
            thumbnailUrl = "https://img.youtube.com/vi/W-rC6bZ6z5I/hqdefault.jpg",
            mediaUrl = "https://www.youtube.com/watch?v=W-rC6bZ6z5I",
            duration = "52:40",
            isFeatured = true,
            viewsCount = 38100
        ),
        ContentItem(
            id = "v_3",
            title = "श्रीमद्भगवद्गीता का यथार्थ रहस्य",
            description = "गीता अध्याय 15 और 18 के गूढ़ रहस्यों का सरल व प्रमाणिक उद्घाटन।",
            type = MediaType.VIDEO,
            category = KnowledgeCategory.GRANTH_SAAR,
            playlist = "Sant Rampal Ji Maharaj Satsang",
            thumbnailUrl = "https://img.youtube.com/vi/kYQG2L0hGZs/hqdefault.jpg",
            mediaUrl = "https://www.youtube.com/watch?v=kYQG2L0hGZs",
            duration = "61:05",
            isFeatured = true,
            viewsCount = 67300
        ),
        ContentItem(
            id = "v_4",
            title = "सभी धर्मों के धर्मग्रंथों में कबीर साहेब",
            description = "कुरान मजीद, बाईबल, गुरु ग्रंथ साहिब और हिन्दू वेदों में एक ही परमात्मा की गवाही।",
            type = MediaType.VIDEO,
            category = KnowledgeCategory.ALL_RELIGIONS,
            playlist = "Spiritual Knowledge",
            thumbnailUrl = "https://img.youtube.com/vi/4yJc9q-2a7w/hqdefault.jpg",
            mediaUrl = "https://www.youtube.com/watch?v=4yJc9q-2a7w",
            duration = "44:30",
            isFeatured = true,
            viewsCount = 29400
        ),
        ContentItem(
            id = "v_5",
            title = "भक्ति बोध - नित्य नियम की महिमा",
            description = "प्रातः एवं संध्या कालीन उपासना का वास्तविक लाभ एवं विधि।",
            type = MediaType.VIDEO,
            category = KnowledgeCategory.BHAKTI_BODH,
            playlist = "Bhakti Bodh",
            thumbnailUrl = "https://img.youtube.com/vi/0k5j-eLgX2E/hqdefault.jpg",
            mediaUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E",
            duration = "35:20",
            isFeatured = false,
            viewsCount = 18900
        ),
        ContentItem(
            id = "v_6",
            title = "शास्त्रार्थ का शंखनाद - पाखण्ड पर ज्ञान का प्रहार",
            description = "सद्ग्रंथों के विरुद्ध की जा रही मनमुखी पूजा का तार्किक व प्रमाणिक खंडन।",
            type = MediaType.VIDEO,
            category = KnowledgeCategory.SHASTRARTH,
            playlist = "Shastrarth",
            thumbnailUrl = "https://img.youtube.com/vi/W-rC6bZ6z5I/hqdefault.jpg",
            mediaUrl = "https://www.youtube.com/watch?v=W-rC6bZ6z5I",
            duration = "58:00",
            isFeatured = false,
            viewsCount = 41200
        )
    )

    val audios = listOf(
        ContentItem(
            id = "a_1",
            title = "असुर निकन्दन रमैणी (स्वरमय पाठ)",
            description = "पवित्र भक्ति बोध से असुर निकन्दन रमैणी का मधुर पाठ।",
            type = MediaType.AUDIO,
            category = KnowledgeCategory.BHAKTI_BODH,
            playlist = "भक्ति बोध ऑडियो",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/05/16/audio_c97693510e.mp3",
            duration = "12:45",
            viewsCount = 15300
        ),
        ContentItem(
            id = "a_2",
            title = "कबीर अमृतवाणी - साखी व सबद",
            description = "सद्गुरु कबीर साहेब जी की आध्यात्मिक अमृतवाणी का गायन।",
            type = MediaType.AUDIO,
            category = KnowledgeCategory.SPIRITUAL_MUSIC,
            playlist = "आध्यात्मिक संगीत",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/03/15/audio_c8c8a73467.mp3",
            duration = "18:30",
            viewsCount = 22100
        ),
        ContentItem(
            id = "a_3",
            title = "संध्या आरती एवं विनती",
            description = "दैनिक संध्या कालीन आरती और मंगलाचरण।",
            type = MediaType.AUDIO,
            category = KnowledgeCategory.BHAKTI_BODH,
            playlist = "भक्ति बोध ऑडियो",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/10/14/audio_9939f792cb.mp3",
            duration = "15:20",
            viewsCount = 19400
        ),
        ContentItem(
            id = "a_4",
            title = "ज्ञान गंगा ऑडियो बुक (अध्याय 1)",
            description = "पुस्तक 'ज्ञान गंगा' का सम्पूर्ण वाचन - मानव जीवन का उद्देश्य।",
            type = MediaType.AUDIO,
            category = KnowledgeCategory.AUDIO_BOOK,
            playlist = "ऑडियो बुक",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/11/06/audio_92429f43f0.mp3",
            duration = "24:10",
            viewsCount = 31200
        ),
        ContentItem(
            id = "a_5",
            title = "जीने की राह ऑडियो बुक (भाग 1)",
            description = "पुस्तक 'जीने की राह' - आदर्श जीवन जीने का व्यावहारिक दर्शन।",
            type = MediaType.AUDIO,
            category = KnowledgeCategory.AUDIO_BOOK,
            playlist = "ऑडियो बुक",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/05/16/audio_c97693510e.mp3",
            duration = "29:50",
            viewsCount = 27800
        ),
        ContentItem(
            id = "a_6",
            title = "गीता तेरा ज्ञान अमृत (ऑडियो सारांश)",
            description = "श्रीमद्भगवद्गीता के 18 अध्यायों का यथार्थ निष्कर्ष।",
            type = MediaType.AUDIO,
            category = KnowledgeCategory.AUDIO_BOOK,
            playlist = "ऑडियो बुक",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/03/15/audio_c8c8a73467.mp3",
            duration = "32:15",
            viewsCount = 18600
        )
    )

    val photoAlbums = listOf(
        PhotoAlbum(
            id = "alb_1",
            title = "सत्संग एवं समागम दर्शन",
            description = "सतलोक आश्रम में आयोजित विशाल समागम एवं सत्संग के दिव्य क्षण।",
            coverUrl = "https://images.unsplash.com/photo-1545239351-ef35f43d514b?auto=format&fit=crop&w=600&q=80",
            photos = listOf(
                PhotoItem("p_1", "विशाल सत्संग पांडाल", "https://images.unsplash.com/photo-1545239351-ef35f43d514b?auto=format&fit=crop&w=800&q=80", "लाखों श्रद्धालुओं की पावन उपस्थिति"),
                PhotoItem("p_2", "अमरवाणी श्रवण", "https://images.unsplash.com/photo-1518241353330-0f7941c2d9b5?auto=format&fit=crop&w=800&q=80", "तत्वज्ञान की अमृत वर्षा"),
                PhotoItem("p_3", "अखंड भंडारा सेवा", "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=800&q=80", "अमृतमयी निःशुल्क महाभंडारा")
            )
        ),
        PhotoAlbum(
            id = "alb_2",
            title = "पवित्र शास्त्रों के छायाचित्र व प्रमाण",
            description = "चारों वेद, गीता, पुराण और कुरान शरीफ के मूल पृष्ठों के प्रामाणिक छायाचित्र।",
            coverUrl = "https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?auto=format&fit=crop&w=600&q=80",
            photos = listOf(
                PhotoItem("p_4", "ऋग्वेद मण्डल 9 पृष्ठ", "https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?auto=format&fit=crop&w=800&q=80", "कविर्देव नाम का स्पष्ट संस्कृत श्लोक"),
                PhotoItem("p_5", "गीता अध्याय 15 श्लोक 1-4", "https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?auto=format&fit=crop&w=800&q=80", "संसार रूपी उल्टे वृक्ष का निरूपण"),
                PhotoItem("p_6", "कुरान शरीफ सूरह फुरकान 25", "https://images.unsplash.com/photo-1507842229458-5770c0c7a52e?auto=format&fit=crop&w=800&q=80", "अल्लजी खलकस्समावाति - कबीरन प्रमाण")
            )
        ),
        PhotoAlbum(
            id = "alb_3",
            title = "अमर संदेश एवं ज्ञान दर्शन",
            description = "कबीर साहेब जी के दोहे और आध्यात्मिक संदेशों के प्रेरणादायक पोस्टर।",
            coverUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=600&q=80",
            photos = listOf(
                PhotoItem("p_7", "कबीर साहेब वाणी पोस्टर", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "मानुष जन्म दुर्लभ है, मिले न बारम्बार"),
                PhotoItem("p_8", "सतलोक का नक्शा", "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=800&q=80", "सृष्टि के 21 ब्रह्मांड और अमरलोक का विहंगम चित्र")
            )
        )
    )

    val audioBooks = listOf(
        ContentItem(
            id = "bk_1",
            title = "ज्ञान गंगा (Gyan Ganga)",
            description = "सर्व धर्मों के पवित्र शास्त्रों का सारगर्भित तत्वज्ञान। विश्व की सर्वाधिक पढ़ी जाने वाली आध्यात्मिक पुस्तक।",
            type = MediaType.BOOK,
            category = KnowledgeCategory.AUDIO_BOOK,
            playlist = "Audio Books",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/11/06/audio_92429f43f0.mp3",
            duration = "04:15:00",
            viewsCount = 112000
        ),
        ContentItem(
            id = "bk_2",
            title = "जीने की राह (Way of Living)",
            description = "नशामुक्ति, दहेजमुक्ति, समाज सुधार और गृहस्थ धर्म का पालन करते हुए मोक्ष प्राप्ति का मार्गदर्शन।",
            type = MediaType.BOOK,
            category = KnowledgeCategory.AUDIO_BOOK,
            playlist = "Audio Books",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/05/16/audio_c97693510e.mp3",
            duration = "05:30:00",
            viewsCount = 98500
        ),
        ContentItem(
            id = "bk_3",
            title = "गीता तेरा ज्ञान अमृत",
            description = "श्रीमद्भगवद्गीता के गूढ़ श्लोकों का यथार्थ भावार्थ एवं काल ब्रह्म का रहस्योद्घाटन।",
            type = MediaType.BOOK,
            category = KnowledgeCategory.AUDIO_BOOK,
            playlist = "Audio Books",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/03/15/audio_c8c8a73467.mp3",
            duration = "06:12:00",
            viewsCount = 84000
        ),
        ContentItem(
            id = "bk_4",
            title = "कबीर सागर (संक्षिप्त भावार्थ)",
            description = "परमेश्वर कबीर साहेब द्वारा धर्मदास जी को प्रदान किया गया गूढ़तम सृष्टि रचना ज्ञान।",
            type = MediaType.BOOK,
            category = KnowledgeCategory.AUDIO_BOOK,
            playlist = "Audio Books",
            mediaUrl = "https://cdn.pixabay.com/download/audio/2022/10/14/audio_9939f792cb.mp3",
            duration = "08:45:00",
            viewsCount = 65400
        )
    )

    val shastrarthItems = listOf(
        ContentItem(
            id = "sh_1",
            title = "ब्रह्मा, विष्णु, महेश जन्म-मरण में हैं - अचूक प्रमाण",
            description = "शिव पुराण और देवी भागवत से अकाट्य प्रमाण कि त्रिदेव अमर नहीं हैं, उनका भी जन्म और मरण होता है।",
            type = MediaType.ARTICLE,
            category = KnowledgeCategory.SHASTRARTH,
            playlist = "Shastrarth",
            mediaUrl = "https://www.youtube.com/watch?v=kYQG2L0hGZs",
            duration = "15 min read",
            scriptureRef = "देवी भागवत स्कंध 3 अध्याय 5; शिव पुराण रुद्र संहिता"
        ),
        ContentItem(
            id = "sh_2",
            title = "पूर्ण परमात्मा साकार हैं या निराकार?",
            description = "वेदों, गीता, कुरान और बाईबल के आधार पर सिद्ध कि परमात्मा सहशरीर (साकार) हैं और उनका नाम कबीर है।",
            type = MediaType.ARTICLE,
            category = KnowledgeCategory.SHASTRARTH,
            playlist = "Shastrarth",
            mediaUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E",
            duration = "20 min read",
            scriptureRef = "ऋग्वेद मण्डल 9 सूक्त 96 मन्त्र 17; कुरान सूरह फुरकान 25:59"
        ),
        ContentItem(
            id = "sh_3",
            title = "गीता का ज्ञान किसने दिया? कृष्ण जी या काल ब्रह्म ने?",
            description = "महाभारत के शांति पर्व और गीता अध्याय 11 के आधार पर प्रामाणिक विश्लेषण कि विराट रूप काल ब्रह्म का था।",
            type = MediaType.ARTICLE,
            category = KnowledgeCategory.SHASTRARTH,
            playlist = "Shastrarth",
            mediaUrl = "https://www.youtube.com/watch?v=4yJc9q-2a7w",
            duration = "25 min read",
            scriptureRef = "गीता अध्याय 11 श्लोक 32 'कालोऽस्मि लोकक्षयकृत्प्रवृद्धो'"
        ),
        ContentItem(
            id = "sh_4",
            title = "श्राद्ध एवं पितृ पूजा - शास्त्रों में क्या है सत्य?",
            description = "गीता अध्याय 9 श्लोक 25 के अनुसार पितरों को पूजने वाले पितर बनते हैं, मोक्ष नहीं मिलता।",
            type = MediaType.ARTICLE,
            category = KnowledgeCategory.SHASTRARTH,
            playlist = "Shastrarth",
            mediaUrl = "https://www.youtube.com/watch?v=W-rC6bZ6z5I",
            duration = "18 min read",
            scriptureRef = "गीता अध्याय 9 श्लोक 25; अध्याय 16 श्लोक 23"
        )
    )
}
