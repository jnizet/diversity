delete from indicator_value;
delete from indicator_category;
delete from indicator_ecogesture;
delete from indicator;
delete from category;
delete from ecogesture;
delete from page_element;
delete from page;
delete from image;
delete from app_user;

insert into indicator (id, biom_id, slug) values
    (1, '7be2a5a6-f226-4fa9-a383-79ca56ca8046', 'especes-menacees'),
    (2, 'f2a14850-23a9-43fc-b8d2-56aebb3562f8', 'especes-envahissantes'),
    (3, 'd99e52c9-7c71-47b4-a720-c0878d2993f7', 'especes-endemiques'),
    (4, '8816092b-1ce3-4ae7-815d-019e99ecf545', 'deforestation'),
    (5, 'e1c91e2e-418e-4bd2-bdfe-7e1025f0b907', 'surfaces-mangroves'),
    (6, '10fe181c-e2b5-4267-b587-5f8c21501947', 'nouvelles-especes'),
    (7, '298a3804-bcb0-4fdb-b3b3-31e14be2cac8', 'especes-inventoriees'),
    (8, '0a494ee4-1c21-415e-be5d-b71e8f4b0519', 'evolution-recifs');

insert into indicator_value (id, indicator_id, territory, value, unit) values
    (11, 1, 'OUTRE_MER', 64, null),
    (12, 1, 'REUNION', 40, null),
    (13, 1, 'GUADELOUPE', 14, null),
    (21, 2, 'OUTRE_MER', 5, '%'),
    (22, 2, 'REUNION', 7, '%'),
    (23, 2, 'SAINT_PIERRE_ET_MIQUELON', 3, '%');

insert into category (id, name) values
    (1, 'Écosystèmes'),
    (2, 'Espèces menacées'),
    (3, 'Végétation');

insert into indicator_category (indicator_id, category_id) values
    (1, 1),
    (2, 1),
    (2, 3);

insert into ecogesture (id, slug) values
    (1, 'recifs');

insert into indicator_ecogesture (indicator_id, ecogesture_id) values
    (1, 1),
    (2, 1);

insert into image (id, content_type, original_file_name) values
    (1, 'image/jpeg', 'carousel1.png'),
    (2, 'image/jpeg', 'carousel2.png'),
    (3, 'image/jpeg', 'carousel3.png'),
    (300, 'image/jpeg', 'carousel4.png'),
    (4, 'image/png', 'apropos.png'),
    (5, 'image/jpeg', 'témoignage.jpg'),
    (6, 'image/jpeg', 'science-participative.png'),
    (7, 'image/png', 'fond.png'),
    (8, 'image/png', 'logo1.png'),
    (9, 'image/png', 'logo2.png'),
    (10, 'image/png', 'logo3.png'),
    (30, 'image/png', 'ecogeste.png'),
    (31, 'image/png', 'fiche-technique.png'),
    (32, 'image/png', 'comprendre.png'),
    (33, 'image/png', 'vignette1.png'),
    (34, 'image/png', 'vignette2.png'),
    (40, 'image/jpeg', 'indicateurs.jpg'),
    (91, 'image/png', 'ecogestes.png'),
    (101, 'image/png', 'interest1.png'),
    (102, 'image/png', 'interest2.png'),
    (103, 'image/png', 'indicators1.png'),
    (104, 'image/png', 'papangue.png'),
    (105, 'image/png', 'other.png'),
    (106, 'image/png', 'reunion.png'),
    (107, 'image/png', 'st-pierre-et-miquelon.png.png'),
    (108, 'image/png', 'tourbieres.png'),
    (109, 'image/png', 'ressources-naturelles.png');

insert into page (id, name, model_name, title) values
   (1, 'Home', 'home', 'Accueil'),
   (2, 'About', 'about', 'À propos'),
   (3, 'recifs', 'ecogesture', 'Écogeste : protéger les récifs'),
   (4, 'EcoGestureHome', 'ecogestures', 'Écogestes'),
   (10, 'reunion', 'territory', 'La Réunion'),
   (11, 'st-pierre-et-miquelon', 'territory', 'Saint Pierre et Miquelon'),
   (29, 'IndicatorHome', 'indicators', 'Indicateurs'),
   (30, 'especes-envahissantes', 'indicator', 'Espèces envahissantes'),
   (31, 'deforestation', 'indicator', 'Déforestation');

insert into page_element (id, page_id, type, key, text, image_id, alt, href, title) values
--     Home
    (nextval('page_element_seq'), 1, 'TEXT', 'carousel.title', 'Découvrez la biodiversité des Outre-Mer', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'carousel.text', 'Partez à la rencontre des espèces et des écosystèmes des territoires français d''outre-mer', null, null, null, false),
    (nextval('page_element_seq'), 1, 'IMAGE', 'carousel.images.0.image', null, 1, 'Chelonia mydas ©  B. Guichard', null, false),
    (nextval('page_element_seq'), 1, 'IMAGE', 'carousel.images.1.image', null, 2, 'Légende n°2', null, false),
    (nextval('page_element_seq'), 1, 'IMAGE', 'carousel.images.2.image', null, 3, 'Légende n°3', null, false),
    (nextval('page_element_seq'), 1, 'IMAGE', 'carousel.images.3.image', null, 300, 'Légende n°4', null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'carousel.territoriesButton', 'Découvrir les territoires', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'presentation.title', 'Le compteur de biodiversité Outre-Mer', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'presentation.text', 'Les territoires d''outre-mer présentent une biodiversité particulièrement riche et variée, mais fragilisée par les activités humaines. La mission du compteur est de donner une vision d''ensemble des enjeux liés à la biodiversité en outre-mer afin que chacun, résident ou voyageur de passage, puisse s''informer et s''impliquer à son échelle pour la préservation de cette biodiversité exceptionnelle.', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'presentation.indicators', 'Des chiffres clés pour comprendre', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'presentation.ecogestures', 'Des gestes simples à mettre en place au quotidien', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'presentation.science', 'Participez aux programmes pour faire progresser la connaissance', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'presentation.territories', 'Découvrez l''identité de chaque territoire d’outre-mer', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'presentation.quote', 'Les outre-mer regroupent 80 % de la biodiversité française, réinventons, adaptons notre façon de vivre et de voyager', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'testimony.title', 'Une biodiversité unique et fragile, protégeons-la !', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'testimony.text', 'La conservation de la biodiversité dépend de sa connaissance et de la compréhension des phénomènes qui l''impactent. Les indicateurs sont des outils qui permettent de suivre différents paramètres de la biodiversité, pour renseigner de son évolution.', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'testimony.quote', 'Ces actions conjuguées entraînent un accroissement de la biodiversité, une amélioration de la qualité de l''eau, de l''air mais aussi de la qualité de vie.', null, null, null, false),
    (nextval('page_element_seq'), 1, 'IMAGE', 'testimony.image', null, 5, 'Témoignage', null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'science.title', 'La photo-identification de serpents de mer par un groupe de plongeuses amatrices', null, null, null, false),
    (nextval('page_element_seq'), 1, 'TEXT', 'science.text', 'Surnommées les « grand-mères fantastiques », un groupe de 7 plongeuses entre 60 et 75 ans ont collaboré avec les chercheurs de l’Université de Nouvelle-Calédonie pour étudier un serpent de mer méconnu, l''Hydrophis major, dans la baie des Citrons, au sud de Nouméa. Leurs photographies très régulières ont permis de recenser 250 individus différents de ce serpent dont on pensait la présence anecdotique.', null, null, null, false),
    (nextval('page_element_seq'), 1, 'IMAGE', 'science.image', null, 6, 'Science participative', null, false),

--     About
    (nextval('page_element_seq'), 2, 'TEXT', 'header.title', 'Pourquoi un compteur de la biodiversité en outre-mer ?', null, null, null, true),
    (nextval('page_element_seq'), 2, 'TEXT', 'header.subtitle', 'Partager la connaissance et encourager chacun...', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'header.background', null, 7, 'Fond', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'header.paragraphs.0.text', 'Les territoires d''outre-mer présentent...', null, null, null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'header.paragraphs.1.text', 'La mission du compteur est de...', null, null, null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'carousel.0.title', 'Partager la connaissance scientifique', null, null, null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'carousel.0.text', 'Quelles sont les espèces présentes sur ce territoire...', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'carousel.0.image', null, 1, '1', null, false),
    (nextval('page_element_seq'), 2, 'LINK', 'carousel.0.link', 'Voir tous les indicateurs', null, null, '/indicators', false),
    (nextval('page_element_seq'), 2, 'TEXT', 'partners.title', 'Ils contribuent au compteur de la biodiversité', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'partners.partners.0.logo', null, 8, 'Logo1', null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'partners.partners.1.logo', null, 9, 'Logo2', null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'partners.partners.2.logo', null, 10, 'Logo3', null, false),

--     Ecogesture 1
    (nextval('page_element_seq'), 3, 'TEXT', 'presentation.name', 'Protégeons les récifs coralliens', null, null, null, true),
    (nextval('page_element_seq'), 3, 'TEXT', 'presentation.category', 'Loisirs', null, null, null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'presentation.description', 'Sinon ils vont mourir', null, null, null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'presentation.image', null, 30, 'Jolis coraux', null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'presentation.file', null, 31, 'Fiche technique', null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'understand.title', 'Comprendre : un écosystème très riche', null, null, null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'understand.text', 'Les récifs coralliens affichent plus d''un tiers des espèces marines connues...', null, null, null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'understand.image', null, 32, 'Comprendre', null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'action.title', 'Les bons gestes pour protéger les récifs', null, null, null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'action.cards.0.icon', null, 33, 'Crème solaire', null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'action.cards.0.description', 'Je choisis une crème solaire non nocive pour l''environnement', null, null, null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'action.cards.1.icon', null, 34, 'Bateau', null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'action.cards.1.description', 'En bâteau, je ne jette pas l''ancre à proximité de récifs', null, null, null, false),

--     Ecogestures home
    (nextval('page_element_seq'), 4, 'TEXT', 'title', 'Réinventons notre façon de vivre et de voyager grâce aux écogestes', null, null, null, true),
    (nextval('page_element_seq'), 4, 'TEXT', 'presentation', 'Lorem ipsum dolor', null, null, null, false),
    (nextval('page_element_seq'), 4, 'IMAGE', 'image', null, 91, 'Ecogestes', null, false),

--     Territory
--     Reunion
    (nextval('page_element_seq'), 10, 'TEXT', 'name',  'La Réunion', null, null, null, true),
    (nextval('page_element_seq'), 10, 'TEXT', 'identity.title',  'Un climat tropical', null, null, null, true),
    (nextval('page_element_seq'), 10, 'TEXT', 'identity.presentation',  'À l''ouest de l''Océan Indien...', null, null, null, false),
    (nextval('page_element_seq'), 10, 'IMAGE', 'identity.infography', null, 106, 'Infographie Réunion', null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'interests.title', 'À la découverte d''endroits emblématiques', null, null, null, false),
    (nextval('page_element_seq'), 10, 'IMAGE', 'interests.images.0.image', null, 101, '1', null, false),
    (nextval('page_element_seq'), 10, 'IMAGE', 'interests.images.1.image', null, 102, '2', null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'indicators.title', 'Indicateurs', null, null, null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'indicators.indicators.0.name', 'Espèces inventoriées à la Réunion', null, null, null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'indicators.indicators.0.value', '4123', null, null, null, false),
    (nextval('page_element_seq'), 10, 'IMAGE', 'indicators.indicators.0.image', null, 103, 'Espèces inventoriées à la Réunion', null, false),
    (nextval('page_element_seq'), 10, 'LINK', 'indicators.indicators.0.link', 'Voir les espèces inventoriées ', null, null, '/indicators/especes-inventoriee-la-reunion', false),
    (nextval('page_element_seq'), 10, 'TEXT', 'indicators.indicators.1.name', 'Surface des forêts', null, null, null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'indicators.indicators.1.value', '219000', null, null, null, false),
    (nextval('page_element_seq'), 10, 'IMAGE', 'indicators.indicators.1.image', null, 103, 'Surface des forêts', null, false),
    (nextval('page_element_seq'), 10, 'LINK', 'indicators.indicators.1.link', 'Voir les forêts ', null, null, '/indicators/forêts-la-reunion', false),
    (nextval('page_element_seq'), 10, 'TEXT', 'species.title', 'Espèces', null, null, null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'species.species.0.name', 'Papangue', null, null, null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'species.species.0.description', 'Dernier rapace de la Réunion', null, null, null, false),
    (nextval('page_element_seq'), 10, 'IMAGE', 'species.species.0.image', null, 104, 'Papangue', null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'ecosystems.title', 'Écosystèmes', null, null, null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'timeline.title', 'Frise chronologique', null, null, null, false),
    (nextval('page_element_seq'), 10, 'TEXT', 'risks.title', 'Risques et enjeux', null, null, null, false),
    (nextval('page_element_seq'), 10, 'IMAGE', 'other.image', null, 105, 'Autre territoire', null, false),
    (nextval('page_element_seq'), 10, 'LINK', 'other.link', 'Portail local de l''environnement', null, null, 'https://oeil.nc', false),

--     St-Pierre-Et-Miquelon
    (nextval('page_element_seq'), 11, 'TEXT', 'name',  'Saint-Pierre-et-Miquelon', null, null, null, true),
    (nextval('page_element_seq'), 11, 'TEXT', 'identity.title',  'Un climat subarctique, froid et humide', null, null, null, true),
    (nextval('page_element_seq'), 11, 'TEXT', 'identity.presentation',  'Au sud de Terre-Neuve...', null, null, null, false),
    (nextval('page_element_seq'), 11, 'IMAGE', 'identity.infography', null, 107, 'Infographie Saint-Pierre-et-Miquelon', null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'interests.title', 'À la découverte d''endroits emblématiques', null, null, null, false),
    (nextval('page_element_seq'), 11, 'IMAGE', 'interests.images.0.image', null, 101, '1', null, false),
    (nextval('page_element_seq'), 11, 'IMAGE', 'interests.images.1.image', null, 102, '2', null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'indicators.title', 'Indicateurs', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'indicators.indicators.0.name', 'Espèces inventoriées à Saint-Pierre-Et-Miquelon', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'indicators.indicators.0.value', '2083', null, null, null, false),
    (nextval('page_element_seq'), 11, 'IMAGE', 'indicators.indicators.0.image', null, 103, 'Espèces inventoriées à Saint-Pierre-Et-Miquelon', null, false),
    (nextval('page_element_seq'), 11, 'LINK', 'indicators.indicators.0.link', 'Voir les espèces inventoriées ', null, null, '/indicators/especes-inventoriee-st-pierre-et-miquelon', false),
    (nextval('page_element_seq'), 11, 'TEXT', 'species.title', 'Espèces', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'species.species.0.name', 'Cerf de Virginie', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'species.species.0.description', 'Peu de mammifères terrestres...', null, null, null, false),
    (nextval('page_element_seq'), 11, 'IMAGE', 'species.species.0.image', null, 104, 'Cerf de Virginie', null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'ecosystems.title', 'Écosystèmes', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'ecosystems.ecosystems.0.name', 'Les marais tourbeux', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'ecosystems.ecosystems.0.description', 'Ces zones humides...', null, null, null, false),
    (nextval('page_element_seq'), 11, 'IMAGE', 'ecosystems.ecosystems.0.image', null, 108, 'Tourbières', null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'timeline.title', 'Frise chronologique', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'timeline.events.0.name', '1535', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'timeline.events.0.description', 'Prise de possession française...', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'timeline.events.1.name', '17ème siècle', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'timeline.events.1.description', 'Premières installations permanentes...', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'risks.title', 'Risques et enjeux', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'risks.risks.0.name', 'Les ressources naturelles', null, null, null, false),
    (nextval('page_element_seq'), 11, 'TEXT', 'risks.risks.0.description', 'Le territoire est pour l''instant...', null, null, null, false),
    (nextval('page_element_seq'), 11, 'IMAGE', 'risks.risks.0.image', null, 109, 'Ressources naturelles', null, false),
    (nextval('page_element_seq'), 11, 'IMAGE', 'other.image', null, 105, 'Autre territoire', null, false),
    (nextval('page_element_seq'), 11, 'LINK', 'other.link', 'Portail local de l''environnement', null, null, 'https://oeil.nc', false),

--     Indicator home
    (nextval('page_element_seq'), 29, 'TEXT', 'title', 'Compter la biodiversité, oui mais comment ?', null, null, null, true),
    (nextval('page_element_seq'), 29, 'TEXT', 'presentation', 'Les indicateurs sont des outils qui permettent de suivre différents paramètres de la biodiversité, pour renseigner de son évolution. De nombreux paramètres sont étudiés : effectifs des populations, état de santé des écosystèmes, taux de pollutions, etc. Ils renseignent ainsi l’état de la biodiversité à un instant précis, mais également l’état des connaissances et des moyens scientifiques actuels.', null, null, null, false),
    (nextval('page_element_seq'), 29, 'IMAGE', 'image', null, 40, 'Chelonia mydas ©  B. Guichard', null, false),
    (nextval('page_element_seq'), 29, 'TEXT', 'understand.title', 'Les indicateurs, des outils pour évaluer la biodiversité', null, null, null, true),
    (nextval('page_element_seq'), 29, 'TEXT', 'understand.description', 'La conservation de la biodiversité dépend de sa connaissance et de la compréhension des phénomènes qui l’impactent. Les indicateurs sont des outils qui permettent de suivre différents paramètres de la biodiversité, pour renseigner de son évolution. De nombreux paramètres sont étudiés : effectifs des populations, état de santé des écosystèmes, taux de pollutions, etc. Cependant, ils connaissent des limites par manque de données disponibles, manque de moyens de recherche ou parce que certains paramètres sont très complexes à évaluer. Ils renseignent ainsi l’état de la biodiversité à un instant précis, mais également l’état des connaissances et des moyens scientifiques actuels.', null, null, null, true),
    (nextval('page_element_seq'), 29, 'IMAGE', 'understand.image', null, 5, 'Chelonia mydas ©  B. Guichard', null, false),
    (nextval('page_element_seq'), 29, 'TEXT', 'onb.title', 'L''observatoire national de la biodiversité', null, null, null, true),
    (nextval('page_element_seq'), 29, 'TEXT', 'onb.description', 'Depuis 2012, l''Observatoire national de la biodiversité, actuellement piloté par l''Office français pour la biodiversité (OFB), publie des indicateurs sur l''état de la biodiversité française, avec la contribution de nombreux partenaires. Ils sont régulièrement actualisés et enrichis. Le Compteur met en lumière les indicateurs de l''ONB qui concernent les outre-mer et contribue à la création de nouveaux indicateurs pour ces territoires.', null, null, null, false),
    (nextval('page_element_seq'), 29, 'LINK', 'onb.link', 'Retrouvez les indicateurs sur le site de l''ONB', null, null, 'http://indicateurs-biodiversite.naturefrance.fr/fr/indicateurs/nombre-despeces-enoutremer- parmi-les-plus-envahissantes-au-monde', false),
    (nextval('page_element_seq'), 29, 'TEXT', 'quote', 'Documenter et présenter l''évolution de la biodiversité, c''est permettre à chacun de prendre conscience des enjeux et des menaces.', null, null, null, false),
    (nextval('page_element_seq'), 29, 'TEXT', 'questions.0.question', 'Qu''est-ce que la biodiversité ?', null, null, null, false),
    (nextval('page_element_seq'), 29, 'TEXT', 'questions.0.answer', 'La majorité des territoires d''outre-mer français sont situés dans des régions particulièrement riches en espèces, notamment en espèces endémiques, ce qu''on appelle des points chauds de la biodiversité (« Hotspots » en anglais) (Nouvelle-Calédonie, Antilles, Mayotte, La Réunion, Wallis et Futuna, Polynésie française). Avec la Guyane, la France possède une partie du plus grand massif forestier de la planète, l''Amazonie, et les territoires français réunissent 10 % des récifs coralliens répartis dans trois océans. Certains territoires isolés comme les îles des Terres australes et antarctiques abritent une faune et une flore adaptés à des climats extrêmes.', null, null, null, false),
    (nextval('page_element_seq'), 29, 'TEXT', 'questions.0.quote', 'Avec ses territoires d''outre-mer, la France a donc une responsabilité forte dans la conservation de la biodiversité mondiale.', null, null, null, false),
    (nextval('page_element_seq'), 29, 'TEXT', 'questions.1.question', 'Les outre-mer, des points chauds de biodiversité', null, null, null, false),
    (nextval('page_element_seq'), 29, 'TEXT', 'questions.1.answer', 'Réponse 2', null, null, null, false),
    (nextval('page_element_seq'), 29, 'TEXT', 'questions.1.quote', 'Citation 2', null, null, null, false),

--     Indicator
--     Espèces envahissantes
    (nextval('page_element_seq'), 30, 'TEXT', 'name',  'Espèces envahissantes', null, null, null, true),
    (nextval('page_element_seq'), 30, 'TEXT', 'presentation.description',  'espèces sur les 100...', null, null, null, false),
    (nextval('page_element_seq'), 30, 'IMAGE', 'presentation.image',  null, 104, 'Espèces envahissantes', null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.title',  'Comprendre', null, null, null, false),
    (nextval('page_element_seq'), 30, 'IMAGE', 'understand.image',  null, 104, 'Espèces envahissantes', null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.sections.0.title',  'Raison 1', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.sections.0.description',  'Explication raison 1', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.sections.1.title',  'Raison 2', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.sections.1.description',  'Explication raison 2', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'indicators.title',  'Espèces envahissantes par territoire', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'ecogestures.title', 'Écogestes', null, null, null, false),

--     Indicator
--     Déforestation
    (nextval('page_element_seq'), 31, 'TEXT', 'name',  'Déforestation', null, null, null, true),
    (nextval('page_element_seq'), 31, 'TEXT', 'presentation.description',  'de la forêt disparaît...', null, null, null, false),
    (nextval('page_element_seq'), 31, 'IMAGE', 'presentation.image',  null, 105, 'Forêt', null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.title',  'Comprendre', null, null, null, false),
    (nextval('page_element_seq'), 31, 'IMAGE', 'understand.image',  null, 105, 'Forêt', null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.sections.0.title',  'Raison 1', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.sections.0.description',  'Explication raison 1', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.sections.1.title',  'Raison 2', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.sections.1.description',  'Explication raison 2', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'indicators.title',  'Déforestation par territoire', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'ecogestures.title', 'Écogestes', null, null, null, false),

-- Users
insert into app_user (id, login, hashed_password) values
   (nextval('app_user_seq'), 'admin', 'x9KbDbdQrtgj+VSVZaeaugL+1ss0J9UeVeX3IjtZ6Qv0QT1s6r2HQw==');

commit;
