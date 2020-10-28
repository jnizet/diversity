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
    (23, 2, 'SAINT_PIERRE_ET_MIQUELON', 3, '%'),
    (24, 4, 'OUTRE_MER', 51, '%');

insert into category (id, name) values
    (1, 'Écosystèmes'),
    (2, 'Espèces menacées'),
    (3, 'Végétation');

insert into indicator_category (indicator_id, category_id) values
    (1, 1),
    (2, 1),
    (2, 3);

insert into ecogesture (id, slug) values
    (1, 'recifs'),
    (2, 'especes-exotiques-envahissantes');

insert into indicator_ecogesture (indicator_id, ecogesture_id) values
    (1, 1),
    (2, 1),
    (4, 1);

insert into image (id, content_type, original_file_name) values
    (1, 'image/jpeg', 'carousel1.png'),
    (2, 'image/jpeg', 'carousel2.png'),
    (3, 'image/jpeg', 'carousel3.png'),
    (300, 'image/jpeg', 'carousel4.png'),
    (4, 'image/png', 'apropos.png'),
    (5, 'image/jpeg', 'témoignage.jpg'),
    (6, 'image/jpeg', 'science-participative.png'),
    (7, 'image/jpeg', 'fond.png'),
    (8, 'image/svg+xml', 'outre-mer.svg'),
    (9, 'image/jpeg', 'onb.jpg'),
    (10, 'image/png', 'mnhn.png'),
    (11, 'image/jpeg', 'apropos1.jpg'),
    (12, 'image/jpeg', 'apropos2.jpg'),
    (13, 'image/jpeg', 'apropos3.jpg'),
    (14, 'image/jpeg', 'apropos-quote.jpg'),
    (29, 'image/jpeg', 'ecogeste_especes_exotiques.jpg'),
    (30, 'image/jpeg', 'ecogeste_corail.jpg'),
    (31, 'application/pdf', 'fiche-technique.pdf'),
    (32, 'image/png', 'comprendre.png'),
    (33, 'image/png', 'vignette1.png'),
    (34, 'image/png', 'vignette2.png'),
    (40, 'image/jpeg', 'indicateurs.jpg'),
    (91, 'image/jpeg', 'ecogeste_intro_all.jpg'),
    (92, 'image/png', 'tous_vivants.png'),
    (101, 'image/png', 'interest1.png'),
    (102, 'image/png', 'interest2.png'),
    (103, 'image/png', 'indicators1.png'),
    (104, 'image/jpeg', 'papangue.jpg'),
    (105, 'image/png', 'other.png'),
    (106, 'image/png', 'reunion.png'),
    (107, 'image/png', 'st-pierre-et-miquelon.png.png'),
    (108, 'image/png', 'tourbieres.png'),
    (109, 'image/png', 'ressources-naturelles.png'),
    (110, 'image/jpeg', 'comprendre.jpg'),
    (201, 'image/jpeg', 'act-background.jpg'),
    (202, 'image/jpeg', 'science-project.jpg'),
    (401, 'image/jpeg', 'science.jpg'),
    (402, 'image/jpeg', 'r1.jpg'),
    (403, 'image/jpeg', 'r2.jpg'),
    (404, 'image/jpeg', 'oiseaux.jpg'),
    (405, 'image/jpeg', 'oiseau.jpg');

insert into page (id, name, model_name, title) values
   (1, 'Home', 'home', 'Accueil'),
   (2, 'About', 'about', 'À propos'),
   (3, 'recifs', 'ecogesture', 'Écogeste : protéger les récifs'),
   (4, 'especes-exotiques-envahissantes', 'ecogesture', 'Écogeste : lutter contre l’introduction d’espèces exotiques envahissantes'),
   (5, 'EcoGestureHome', 'ecogestures', 'Écogestes'),
   (6, 'Act', 'act', 'Agir ensemble'),
   (7, 'Science', 'science', 'Sciences participatives'),
   (8, 'Territories', 'territories', 'Territoires'),
   (9, 'LegalTerms', 'legal-terms', 'Mentions légales'),
   (10, 'reunion', 'territory', 'La Réunion'),
   (11, 'saint-pierre-et-miquelon', 'territory', 'Saint Pierre et Miquelon'),
   (29, 'IndicatorHome', 'indicators', 'Indicateurs'),
   (30, 'especes-envahissantes', 'indicator', 'Espèces envahissantes'),
   (31, 'deforestation', 'indicator', 'Déforestation'),
   (51, 'ecogesture-act', 'ecogesture-act', 'Section agir commune aux pages ecogestes');

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
    (nextval('page_element_seq'), 2, 'TEXT', 'header.subtitle', 'La mission du compteur est de donner une vision d’ensemble des enjeux liés à la biodiversité en outre-mer, et s’articule autour de trois objectifs : ', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'header.background', null, 7, 'Légende', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'goal1.title', 'Partager la connaissance scientifique', null, null, null, true),
    (nextval('page_element_seq'), 2, 'TEXT', 'goal1.description', 'Quelles sont les espèces présentes dans ce territoire ? Comment évoluent leurs populations ? Quel est l’état de santé des récifs coralliens ? Pour répondre à ces questions et bien d’autres, différents paramètres, appelés « indicateurs », sont mesurés par les scientifiques pour suivre l’évolution de l’état de la biodiversité. Le compteur a pour mission de rendre compte de l’état de ces connaissances dans tous les territoires. ', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'goal1.image', null, 11, 'Légende', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'goal2.title', 'Valoriser les actions des territoires', null, null, null, true),
    (nextval('page_element_seq'), 2, 'TEXT', 'goal2.description', 'De nombreuses actions locales sont mises en place afin de tenter de préserver les écosystèmes et les espèces. Donner de la visibilité aux acteurs locaux, partager les expériences et les réussites, au bénéfice de tous, c’est également l’un des objectifs du compteur.', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'goal2.image', null, 12, 'Légende', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'goal2.quote', 'Les territoires d’outre-mer présentent une biodiversité particulièrement riche et variée, mais fragilisée par les activités humaines.', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'goal2.quoteImage', null, 14, 'Légende', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'goal3.title', 'Encourager chacun à agir', null, null, null, true),
    (nextval('page_element_seq'), 2, 'TEXT', 'goal3.description', 'Chacun, à son échelle, peut agir pour préserver la biodiversité. Connaitre et appliquer des gestes simples, modifier ses habitudes et son comportement ou encore s’investir dans des programmes de sciences participatives, tout le monde est concerné par la préservation de la biodiversité. Le rôle du compteur est d’accompagner les citoyens en partageant desinformations claires et adaptées à tous les territoires, et en encourageant à se mobiliser.', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'goal3.image', null, 13, 'Légende', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'partners.title', 'Ils contribuent au compteur de la biodiversité', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'partners.partners.0.logo', null, 8, 'Logo1', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'partners.partners.0.url', 'http://google.com', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'partners.partners.1.logo', null, 9, 'Logo2', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'partners.partners.1.url', 'http://google.com', null, null, null, false),
    (nextval('page_element_seq'), 2, 'IMAGE', 'partners.partners.2.logo', null, 10, 'Logo3', null, false),
    (nextval('page_element_seq'), 2, 'TEXT', 'partners.partners.2.url', 'http://google.com', null, null, null, false),

--     Ecogesture "Act" section
    (nextval('page_element_seq'), 51, 'TEXT', 'title', 'Agir pour la science', null, null, null, true),
    (nextval('page_element_seq'), 51, 'TEXT', 'description', 'La majorité des territoires d’outre-mer français sont situés dans des régions particulièrement riches en espèces, notamment en espèces endémiques, ce qu’on appelle des points chauds de la biodiversité', null, null, null, true),
    (nextval('page_element_seq'), 51, 'TEXT', 'firstActionName', 'J’agis en ligne', null, null, null, false),
    (nextval('page_element_seq'), 51, 'LINK', 'firstActionLink', 'Je télécharge l’application INPN Espaces', null, null, 'https://google.com', false),
    (nextval('page_element_seq'), 51, 'TEXT', 'secondActionName', 'Je donne de mon temps', null, null, null, false),
    (nextval('page_element_seq'), 51, 'LINK', 'secondActionLink', 'Tous les programmes de sciences participatives', null, null, '/sciences-participatives', false),

--     Ecogesture "Recifs coralliens"
    (nextval('page_element_seq'), 3, 'TEXT', 'presentation.name', 'Protégeons les récifs coralliens', null, null, null, true),
    (nextval('page_element_seq'), 3, 'TEXT', 'presentation.category', 'Loisirs', null, null, null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'presentation.description', 'Au sein de l’archipel des Seychelles, le premier projet de restauration des récifs coraliens à grande échelle a débuté', null, null, null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'presentation.image', null, 30, 'Jolis coraux', null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'presentation.file', null, 31, 'Fiche technique', null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'understand.title', 'Comprendre : un écosystème très riche', null, null, null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'understand.text', 'Les récifs coralliens abritent plus d’un tiers des espèces marines connues pour lesquelles ils représentent notamment une zone de nourricerie pour les juvéniles. Ils forment également une barrière naturelle contre la houle, protégeant la côte lors des tempêtes. De nombreux facteurs menacent les coraux : changements climatiques (augmentation de la température et acidification des océans), surexploitation des ressources, pollutions… ', null, null, null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'understand.quote', 'Ces actions conjuguées entraînent un accroissement de la biodiversité, une amélioration de la qualité de l’eau, de l’air mais aussi de la qualité de vie', null, null, null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'action.title', 'Les bons gestes pour protéger les récifs', null, null, null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'action.cards.0.icon', null, 33, 'Crème solaire', null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'action.cards.0.description', 'Je choisis une crème solaire non nocive pour l''environnement', null, null, null, false),
    (nextval('page_element_seq'), 3, 'IMAGE', 'action.cards.1.icon', null, 34, 'Bateau', null, false),
    (nextval('page_element_seq'), 3, 'TEXT', 'action.cards.1.description', 'En bateau, je ne jette pas l''ancre à proximité de récifs', null, null, null, false),

--     Ecogesture "Especes exotiques envahissantes"
    (nextval('page_element_seq'), 4, 'TEXT', 'presentation.name', 'Lutter contre l’introduction d’espèces exotiques envahissantes', null, null, null, true),
    (nextval('page_element_seq'), 4, 'TEXT', 'presentation.category', 'Biodiversité', null, null, null, false),
    (nextval('page_element_seq'), 4, 'TEXT', 'presentation.description', 'Sinon elles sont pas gentilles avec les autres espèces', null, null, null, false),
    (nextval('page_element_seq'), 4, 'IMAGE', 'presentation.image', null, 29, 'Espèce envahissante', null, false),
    (nextval('page_element_seq'), 4, 'IMAGE', 'presentation.file', null, 31, 'Fiche technique', null, false),
    (nextval('page_element_seq'), 4, 'TEXT', 'understand.title', 'Comprendre : un écosystème très riche', null, null, null, false),
    (nextval('page_element_seq'), 4, 'TEXT', 'understand.text', 'Les récifs coralliens abritent plus d’un tiers des espèces marines connues pour lesquelles ils représentent notamment une zone de nourricerie pour les juvéniles. Ils forment également une barrière naturelle contre la houle, protégeant la côte lors des tempêtes. De nombreux facteurs menacent les coraux : changements climatiques (augmentation de la température et acidification des océans), surexploitation des ressources, pollutions… ', null, null, null, false),
    (nextval('page_element_seq'), 4, 'TEXT', 'understand.quote', 'Ces actions conjuguées entraînent un accroissement de la biodiversité, une amélioration de la qualité de l’eau, de l’air mais aussi de la qualité de vie', null, null, null, false),
    (nextval('page_element_seq'), 4, 'TEXT', 'action.title', 'Les bons gestes pour protéger les récifs', null, null, null, false),
    (nextval('page_element_seq'), 4, 'IMAGE', 'action.cards.0.icon', null, 33, 'Crème solaire', null, false),
    (nextval('page_element_seq'), 4, 'TEXT', 'action.cards.0.description', 'Je choisis une crème solaire non nocive pour l''environnement', null, null, null, false),
    (nextval('page_element_seq'), 4, 'IMAGE', 'action.cards.1.icon', null, 33, 'Bateau', null, false),
    (nextval('page_element_seq'), 4, 'TEXT', 'action.cards.1.description', 'En bateau, je ne jette pas l''ancre à proximité de récifs', null, null, null, false),

--     Ecogestures home
    (nextval('page_element_seq'), 5, 'TEXT', 'title', 'Adaptons notre comportement pour préserver la biodiversité', null, null, null, true),
    (nextval('page_element_seq'), 5, 'TEXT', 'presentation', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer rutrum ipsum sed viverra faucibus. Cras eu hendrerit mi. Proin efficitur arcu a metus consectetur, ac fermentum', null, null, null, false),
    (nextval('page_element_seq'), 5, 'IMAGE', 'image', null, 91, 'Ecogestes', null, false),
    (nextval('page_element_seq'), 5, 'TEXT', 'question', 'Qu''est-ce qu''un écogeste ?', null, null, null, false),
    (nextval('page_element_seq'), 5, 'TEXT', 'answer', 'Les territoires d’outre-mer présentent une biodiversité particulièrement riche et variée, mais fragilisée par les activités humaines. Les territoires d’outre-mer présentent une biodiversité particulièrement riche et variée, mais fragilisée par les activités humaines. Les territoires d''outre-mer présentent une biodiversité particulièrement riche.', null, null, null, false),
    (nextval('page_element_seq'), 5, 'TEXT', 'quote', 'Des actions concrètes pour agir dès maintenant et nous donner la possibilité de préserver cette biodiversité riche et fragile', null, null, null, false),
    (nextval('page_element_seq'), 5, 'IMAGE', 'other.image', null, 92, 'Biodiversité. Tous vivants !', null, false),
    (nextval('page_element_seq'), 5, 'TEXT', 'other.title', 'Retrouvez d’autres écogestes sur', null, null, null, false),
    (nextval('page_element_seq'), 5, 'TEXT', 'other.text', 'Biodiversité. Tous vivants !', null, null, null, false),

--     Act together
    (nextval('page_element_seq'), 6, 'TEXT', 'header.title', 'Agir ensemble pour une biodiversité unique mais fragile', null, null, null, true),
    (nextval('page_element_seq'), 6, 'TEXT', 'header.subtitle', 'Les outre-mer abritent une biodiversité unique mais fragile : protégeons-là !', null, null, null, false),
    (nextval('page_element_seq'), 6, 'IMAGE', 'header.background', null, 201, 'Légende', null, false),
    (nextval('page_element_seq'), 6, 'TEXT', 'ecogestures.title', 'Découvrez les écogestes', null, null, null, true),
    (nextval('page_element_seq'), 6, 'TEXT', 'ecogestures.subtitle', 'Découvrez les bons gestes recommandés par le Ministère de l’outre-mer pour une expérience responsable', null, null, null, false),
    (nextval('page_element_seq'), 6, 'TEXT', 'science.title', 'Les sciences participatives', null, null, null, true),
    (nextval('page_element_seq'), 6, 'TEXT', 'science.subtitle', 'Protégez la biodiversité ultra-marine à travers les ciences participatives', null, null, null, false),
    (nextval('page_element_seq'), 6, 'TEXT', 'science.project.title', 'Un projet original en Nouvelle-Calédonie', null, null, null, false),
    (nextval('page_element_seq'), 6, 'TEXT', 'science.project.description', 'Un groupe de 7 plongeuses entre 60 et 75 ans ont collaboré avec les chercheurs de l’Université de Nouvelle-Calédonie pour étudier un serpent de mer méconnu, l’Hydrophis major, dans la baie des Citrons, au sud de Nouméa.', null, null, null, false),
    (nextval('page_element_seq'), 6, 'IMAGE', 'science.project.image', null, 202, 'Légende', null, false),

--     Science
    (nextval('page_element_seq'), 7, 'TEXT', 'header.title', 'Que sont les sciences participatives ?', null, null, null, true),
    (nextval('page_element_seq'), 7, 'TEXT', 'header.subtitle', 'Les sciences participatives permettent à tous les curieux de la nature, du débutant à l’expérimenté, de contribuer à la recherche sur la biodiversité en fournissant aux scientifiques un grand nombre de données de terrain.', null, null, null, false),
    (nextval('page_element_seq'), 7, 'IMAGE', 'header.background', null, 401, 'Légende', null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'presentation.title', 'Sciences faites par des non-professionnels', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'presentation.description', 'Au-delà de la simple définition « sciences faites par des non-professionnels », l’Institut de formation et de recherche en éducation à l’environnement (Ifrée) distingue 3 catégories de programmes participatifs selon leurs objectifs, permettant de mieux comprendre ce que sont réellement les Sciences Participatives.', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'paragraphs.0.title', 'Les bases de données collaboratives', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'paragraphs.0.text', 'Il s’agit de projet d’inventaires et de signalements destinés aux amateurs sachant identifier les taxons observés. Les débutants y sont les bienvenus. L’objet principal est de partager de la connaissance, éventuellement mobilisable a posteriori dans unprojet de recherche ou dans un objectif de conservation.', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'paragraphs.1.title', 'Les projets adossés à des programmes de recherche', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'paragraphs.1.text', 'L’objet et les conditions de collecte des données sont alors très précisément définis, garantissant ainsi l’exploitabilité de ces dernières. La nature du protocole va contraindre le type de public participant, mais cela peut souvent inclure des débutants. Les données collectées peuvent être ensuite versées dans lesprojets de bases de données collaboratives.', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'paragraphs.2.title', 'Les programmes à visée éducative ou de gestion/conservation', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'paragraphs.2.text', 'On trouve là divers programmes de sensibilisation à la démarche scientifique ou aux enjeux de conservation. Certains d’entre eux n’impliquent pas de collecte d’information centralisée, leur objectif étant d’abord de permettre aux participants d’acquérir des connaissances.', null, null, null, false),
    (nextval('page_element_seq'), 7, 'IMAGE', 'images.0.image', null, 402, 'Légende', null, false),
    (nextval('page_element_seq'), 7, 'IMAGE', 'images.1.image', null, 403, 'Légende', null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.title', 'Deux exemples de programmes participatifs en Outre-mer', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.0.title', 'Un projet original en Nouvelle-Calédonie', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.0.description', 'Un groupe de 7 plongeuses entre 60 et 75 ans ont collaboré avec les chercheurs de l’Université de Nouvelle-Calédonie pour étudier un serpent de mer méconnu, l’Hydrophis major, dans la baie des Citrons, au sud de Nouméa.', null, null, null, false),
    (nextval('page_element_seq'), 7, 'LINK', 'examples.projects.0.more', 'En savoir plus', null, null, 'https://google.com', false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.0.subject', 'Suivi des populations d’oiseaux communs par échantillonnages ponctuels simples le long de parcours prédéfinis', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.0.actor', 'Groupe d’Etude et de Protection des Oiseaux de Guyane (GEPOG)', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.0.target', 'Ornithologues à titre bénévole ou dans un cadre professionnels (Agents de zones protégées)', null, null, null, false),
    (nextval('page_element_seq'), 7, 'IMAGE', 'examples.projects.0.image', null, 404, 'Légende', null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.1.title', 'Autre titre', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.1.description', 'Un groupe de 7 plongeuses entre 60 et 75 ans ont collaboré avec les chercheurs de l’Université de Nouvelle-Calédonie pour étudier un serpent de mer méconnu, l’Hydrophis major, dans la baie des Citrons, au sud de Nouméa.', null, null, null, false),
    (nextval('page_element_seq'), 7, 'LINK', 'examples.projects.1.more', 'En savoir plus', null, null, 'https://google.com', false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.1.subject', 'Suivi des populations d’oiseaux communs par échantillonnages ponctuels simples le long de parcours prédéfinis', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.1.actor', 'Groupe d’Etude et de Protection des Oiseaux de Guyane (GEPOG)', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'examples.projects.1.target', 'Ornithologues à titre bénévole ou dans un cadre professionnels (Agents de zones protégées)', null, null, null, false),
    (nextval('page_element_seq'), 7, 'IMAGE', 'examples.projects.1.image', null, 405, 'Légende', null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'application.title', 'Agir pour la science depuis son smartphone, c''est possible !', null, null, null, false),
    (nextval('page_element_seq'), 7, 'TEXT', 'application.subtitle', 'J’agis en ligne', null, null, null, false),
    (nextval('page_element_seq'), 7, 'LINK', 'application.downloadLink', 'Je télécharge l''application INPN Espaces', null, null, 'https://google.com', false),

--     Territories
    (nextval('page_element_seq'), 8, 'TEXT', 'header.title', 'A la découverte des Outre-mer', null, null, null, true),
    (nextval('page_element_seq'), 8, 'TEXT', 'header.text', 'Les territoires d’outre-mer présentent une biodiversité particulièrement riche et variée, mais fragilisée par les activités humaines. Naviguez parmi les portraits biodiversité des différents territoires et découvrez l’histoire des espèces et des écosystèmes qui les peuplent, les enjeux de chaque territoire et les initiatives des acteurs locaux pour les préserver.', null, null, null, false),
    (nextval('page_element_seq'), 8, 'TEXT', 'header.population', '6 274', null, null, null, false),
    (nextval('page_element_seq'), 8, 'TEXT', 'header.species', '85 117', null, null, null, false),

--     Legal terms
    (nextval('page_element_seq'), 9, 'TEXT', 'title', 'Mentions légales', null, null, null, true),
    (nextval('page_element_seq'), 9, 'TEXT', 'paragraphs.0.title', 'Clause 1', null, null, null, false),
    (nextval('page_element_seq'), 9, 'TEXT', 'paragraphs.0.text', 'Bla bla bla', null, null, null, false),
    (nextval('page_element_seq'), 9, 'TEXT', 'paragraphs.1.title', 'Clause 2', null, null, null, false),
    (nextval('page_element_seq'), 9, 'TEXT', 'paragraphs.1.text', 'Bla bla bla', null, null, null, false),

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
    (nextval('page_element_seq'), 30, 'TEXT', 'presentation.description',  'des espèces considérées comme les plus envahissantes au monde se trouvent en outre-mer.', null, null, null, false),
    (nextval('page_element_seq'), 30, 'IMAGE', 'presentation.image',  null, 104, 'Espèces envahissantes', null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'presentation.onbUrl',  'http://indicateurs-biodiversite.naturefrance.fr/fr/indicateurs/nombre-despeces-en-outremer-parmi-les-plus-envahissantes-au-monde', null, null, null, false),
    (nextval('page_element_seq'), 30, 'IMAGE', 'understand.image',  null, 110, 'Espèces envahissantes', null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.title1',  'Qu’est-ce qu’une espèce exotique envahissante ?', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.text1',  'Une espèce exotique envahissante est une espèce introduite par l’Homme hors de son territoire d’origine et qui présente, sur son territoire d’introduction, une dispersion rapide et des impacts environnementaux mais également parfois sociaux et économiques. Ces introductions sont parfois volontaires (pour l’agriculture, l’ornementation, comme animaux de compagnie, etc.) ou involontaires (via la multiplication des voyages internationaux, « passagers clandestins » dans les marchandises échangées, dans les eaux de ballast des bateaux, etc.).', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.title2',  'Espèces exotiques vs. espèces indigènes', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.text2',  'Si de nombreuses espèces n’arrivent pas à s’adapter ou se reproduire dans leurs territoires d’introduction, certaines s’adaptent au contraire très bien, se développant en l’absence de leurs prédateurs ou parasites naturels. Elles peuvent alors causer de lourds impacts, en particulier dans les îles où les écosystèmes sont plus fragiles, par compétition avec les espèces indigènes, prédation, hybridation, modification des habitats, etc.', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.keyword',  'Espèces', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.paragraphs.0.title',  'Une liste mondiale des espèces les plus problématiques', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'understand.paragraphs.0.text',  'En 2007, l’Union international pour la conservation de la nature a publié une liste de 100 espèces exotiques considérées comme les plus envahissantes du monde, c’est-à-dire celles avec le plus fort potentiel de dispersion et le plus lourd impact sur leur environnement d’accueil. On y trouve notamment le rat noir (Rattus rattus), introduit dans de nombreuses îles et responsable, entre autres, du déclin des populations de nombreuses espèces d’oiseau dans les outre-mer, ou encore le Miconia (Miconia calvescens) introduit à Tahiti en 1937 et aujourd’hui très répandu sur l’île où il remplace la végétation indigène. Ce dernier a également été introduit en Nouvelle-Calédonie et découvert récemment en Martinique et en Guadeloupe. De nombreuses interventions sont réalisées pour éradiquer ces espèces ou à défaut tenter de limiter leur propagation et leurs impacts, et de nombreuses collectivités mettent en œuvre des processus de biosécurité à l’entrée des territoires afin de limiter les risques d’entrée d’espèces exotiques.', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'territories.title',  'Nombre d’espèces exotiques envahissantes par territoires', null, null, null, false),
    (nextval('page_element_seq'), 30, 'TEXT', 'ecogestures.title', 'Que puis-je faire pour éviter l’introduction et la dispersion d’espèces exotiques envahissantes ?', null, null, null, false),

--     Indicator
--     Déforestation
    (nextval('page_element_seq'), 31, 'TEXT', 'presentation.description',  'de la forêt disparaît...', null, null, null, false),
    (nextval('page_element_seq'), 31, 'IMAGE', 'presentation.image',  null, 11, 'Forêt', null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'presentation.onbUrl',  'http://indicateurs-biodiversite.naturefrance.fr/fr/indicateurs/nombre-despeces-en-outremer-parmi-les-plus-envahissantes-au-monde', null, null, null, false),
    (nextval('page_element_seq'), 31, 'IMAGE', 'understand.image',  null, 110, 'Forêt', null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.title1',  'Raison 1', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.text1',  'Explication raison 1', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.title2',  'Raison 2', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.text2',  'Explication raison 2', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'understand.keyword',  'Forêts', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'territories.title',  'Déforestation par territoire', null, null, null, false),
    (nextval('page_element_seq'), 31, 'TEXT', 'ecogestures.title', 'Écogestes', null, null, null, false);

-- Users
insert into app_user (id, login, hashed_password) values
   (nextval('app_user_seq'), 'admin', 'x9KbDbdQrtgj+VSVZaeaugL+1ss0J9UeVeX3IjtZ6Qv0QT1s6r2HQw==');

commit;
