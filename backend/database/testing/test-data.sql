delete from page_element;
delete from page;
delete from image;
delete from indicator_value;
delete from indicator_category;
delete from indicator;
delete from category;

insert into indicator (id, biom_id) values
    (1, 'especes-envahissantes');

insert into indicator_value (id, indicator_id, territory, value, unit) values
    (1, 1, 'OUTRE_MER', 64, null),
    (2, 1, 'REUNION', 40, null),
    (3, 1, 'GUADELOUPE', 14, null);

insert into category (id, name) values
    (1, 'Écosystèmes'),
    (2, 'Espèces menacées');

insert into indicator_category (indicator_id, category_id) values
    (1, 1);

insert into image (id, content_type, original_file_name) values
    (1, 'image/png', 'carousel1.png'),
    (2, 'image/png', 'carousel2.png'),
    (3, 'image/png', 'carousel3.png'),
    (4, 'image/png', 'apropos.png'),
    (5, 'image/png', 'indicateurs.png'),
    (6, 'image/png', 'science-participative.png'),
    (7, 'image/png', 'fond.png'),
    (8, 'image/png', 'logo1.png'),
    (9, 'image/png', 'logo2.png'),
    (10, 'image/png', 'logo3.png'),
    (30, 'image/png', 'ecogeste.png'),
    (31, 'image/png', 'fiche-technique.png'),
    (32, 'image/png', 'comprendre.png'),
    (33, 'image/png', 'vignette1.png'),
    (34, 'image/png', 'vignette2.png'),
    (40, 'image/png', 'indicateurs.png'),
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
   (30, 'especes-envahissantes', 'indicator', 'Espèces envahissantes');

insert into page_element (id, page_id, type, key, text, image_id, alt, href, title) values
--     Home
    (11, 1, 'TEXT', 'carousel.title', 'Ensemble protégeons la diversité des Outre-Mer', null, null, null, false),
    (12, 1, 'TEXT', 'carousel.text', 'Partez à la rencontre des espèces et des écosystèmes des territoires français d''outre-mer', null, null, null, false),
    (13, 1, 'IMAGE', 'carousel.images.0.image', null, 1, '1', null, false),
    (14, 1, 'IMAGE', 'carousel.images.1.image', null, 2, '2', null, false),
    (15, 1, 'IMAGE', 'carousel.images.2.image', null, 3, '3', null, false),
    (16, 1, 'TEXT', 'carousel.territoriesButton', 'Découvrir les territoires', null, null, null, false),
    (17, 1, 'TEXT', 'presentation.title', 'Le compteur de biodiversité Outre-Mer', null, null, null, false),
    (18, 1, 'TEXT', 'presentation.text', 'Les territoires d''outre-mer présentent une biodiversité particulièrement riche et variée, ...', null, null, null, false),
    (19, 1, 'IMAGE', 'presentation.image', null, 4, 'À propos', null, false),
    (20, 1, 'TEXT', 'presentation.more', 'En savoir plus', null, null, null, false),
    (21, 1, 'TEXT', 'indicators.title', 'Une biodiversité unique et fragile, protégeons-la !', null, null, null, false),
    (22, 1, 'TEXT', 'indicators.text', 'La conservation de la biodiversité dépend de sa connaissance ...', null, null, null, false),
    (23, 1, 'IMAGE', 'indicators.image', null, 5, 'Indicateurs', null, false),
    (24, 1, 'TEXT', 'science.title', 'Du « super-corail » pour sauver les récifs', null, null, null, false),
    (25, 1, 'TEXT', 'science.text', 'Au sein de l''archipel des Seychelles le premier projet de restauration des récifs coralliens à grande échelle a débuté.', null, null, null, false),
    (26, 1, 'IMAGE', 'science.image', null, 6, 'Science participative', null, false),

--     About
    (41, 2, 'TEXT', 'header.title', 'Pourquoi un compteur de la biodiversité en outre-mer ?', null, null, null, true),
    (42, 2, 'TEXT', 'header.subtitle', 'Partager la connaissance et encourager chacun...', null, null, null, false),
    (43, 2, 'IMAGE', 'header.background', null, 7, 'Fond', null, false),
    (44, 2, 'TEXT', 'header.paragraphs.0.text', 'Les territoires d''outre-mer présentent...', null, null, null, false),
    (45, 2, 'TEXT', 'header.paragraphs.1.text', 'La mission du compteur est de...', null, null, null, false),
    (46, 2, 'TEXT', 'carousel.0.title', 'Partager la connaissance scientifique', null, null, null, false),
    (47, 2, 'TEXT', 'carousel.0.text', 'Quelles sont les espèces présentes sur ce territoire...', null, null, null, false),
    (48, 2, 'IMAGE', 'carousel.0.image', null, 1, '1', null, false),
    (49, 2, 'LINK', 'carousel.0.link', 'Voir tous les indicateurs', null, null, '/indicators', false),
    (50, 2, 'TEXT', 'partners.title', 'Ils contribuent au compteur de la biodiversité', null, null, null, false),
    (51, 2, 'IMAGE', 'partners.partners.0.logo', null, 8, 'Logo1', null, false),
    (52, 2, 'IMAGE', 'partners.partners.1.logo', null, 9, 'Logo2', null, false),
    (53, 2, 'IMAGE', 'partners.partners.2.logo', null, 10, 'Logo3', null, false),

--     Ecogesture 1
    (81, 3, 'TEXT', 'presentation.name', 'Protégeons les récifs coralliens', null, null, null, true),
    (82, 3, 'TEXT', 'presentation.category', 'Loisirs', null, null, null, false),
    (83, 3, 'TEXT', 'presentation.description', 'Sinon ils vont mourir', null, null, null, false),
    (84, 3, 'IMAGE', 'presentation.image', null, 30, 'Jolis coraux', null, false),
    (85, 3, 'IMAGE', 'presentation.file', null, 31, 'Fiche technique', null, false),
    (86, 3, 'TEXT', 'understand.title', 'Comprendre : un écosystème très riche', null, null, null, false),
    (87, 3, 'TEXT', 'understand.text', 'Les récifs coralliens affichent plus d''un tiers des espèces marines connues...', null, null, null, false),
    (88, 3, 'IMAGE', 'understand.image', null, 32, 'Comprendre', null, false),
    (89, 3, 'TEXT', 'action.title', 'Les bons gestes pour protéger les récifs', null, null, null, false),
    (90, 3, 'IMAGE', 'action.cards.0.icon', null, 33, 'Crème solaire', null, false),
    (91, 3, 'TEXT', 'action.cards.0.description', 'Je choisis une crème solaire non nocive pour l''environnement', null, null, null, false),
    (92, 3, 'IMAGE', 'action.cards.1.icon', null, 34, 'Bateau', null, false),
    (93, 3, 'TEXT', 'action.cards.1.description', 'En bâteau, je ne jette pas l''ancre à proximité de récifs', null, null, null, false),

--     Ecogestures home
    (161, 4, 'TEXT', 'title', 'Réinventons notre façon de vivre et de voyager grâce aux écogestes', null, null, null, true),
    (162, 4, 'TEXT', 'presentation', 'Lorem ipsum dolor', null, null, null, false),
    (163, 4, 'IMAGE', 'image', null, 91, 'Ecogestes', null, false),

--     Territory
--     Reunion
    (200, 10, 'TEXT', 'name',  'La Réunion', null, null, null, true),
    (201, 10, 'TEXT', 'identity.title',  'Un climat tropical', null, null, null, true),
    (202, 10, 'TEXT', 'identity.presentation',  'À l''ouest de l''Océan Indien...', null, null, null, false),
    (203, 10, 'IMAGE', 'identity.infography', null, 106, 'Infographie Réunion', null, false),
    (204, 10, 'TEXT', 'interests.title', 'À la découverte d''endroits emblématiques', null, null, null, false),
    (205, 10, 'IMAGE', 'interests.images.0.image', null, 101, '1', null, false),
    (206, 10, 'IMAGE', 'interests.images.1.image', null, 102, '2', null, false),
    (207, 10, 'TEXT', 'indicators.title', 'Indicateurs', null, null, null, false),
    (208, 10, 'TEXT', 'indicators.indicators.0.name', 'Espèces inventoriées à la Réunion', null, null, null, false),
    (209, 10, 'TEXT', 'indicators.indicators.0.value', '4123', null, null, null, false),
    (210, 10, 'IMAGE', 'indicators.indicators.0.image', null, 103, 'Espèces inventoriées à la Réunion', null, false),
    (211, 10, 'LINK', 'indicators.indicators.0.link', 'Voir les espèces inventoriées ', null, null, '/indicators/especes-inventoriee-la-reunion', false),
    (212, 10, 'TEXT', 'indicators.indicators.1.name', 'Surface des forêts', null, null, null, false),
    (213, 10, 'TEXT', 'indicators.indicators.1.value', '219000', null, null, null, false),
    (214, 10, 'IMAGE', 'indicators.indicators.1.image', null, 103, 'Surface des forêts', null, false),
    (215, 10, 'LINK', 'indicators.indicators.1.link', 'Voir les forêts ', null, null, '/indicators/forêts-la-reunion', false),
    (216, 10, 'TEXT', 'species.title', 'Espèces', null, null, null, false),
    (217, 10, 'TEXT', 'species.species.0.name', 'Papangue', null, null, null, false),
    (218, 10, 'TEXT', 'species.species.0.description', 'Dernier rapace de la Réunion', null, null, null, false),
    (219, 10, 'IMAGE', 'species.species.0.image', null, 104, 'Papangue', null, false),
    (220, 10, 'TEXT', 'ecosystems.title', 'Écosystèmes', null, null, null, false),
    (221, 10, 'TEXT', 'timeline.title', 'Frise chronologique', null, null, null, false),
    (222, 10, 'TEXT', 'risks.title', 'Risques et enjeux', null, null, null, false),
    (223, 10, 'IMAGE', 'other.image', null, 105, 'Autre territoire', null, false),
    (224, 10, 'LINK', 'other.link', 'Portail local de l''environnement', null, null, 'https://oeil.nc', false),

--     St-Pierre-Et-Miquelon
    (300, 11, 'TEXT', 'name',  'Saint-Pierre-et-Miquelon', null, null, null, true),
    (301, 11, 'TEXT', 'identity.title',  'Un climat subarctique, froid et humide', null, null, null, true),
    (302, 11, 'TEXT', 'identity.presentation',  'Au sud de Terre-Neuve (Canada)...', null, null, null, false),
    (303, 11, 'IMAGE', 'identity.infography', null, 107, 'Infographie Saint-Pierre-et-Miquelon', null, false),
    (304, 11, 'TEXT', 'interests.title', 'À la découverte d''endroits emblématiques', null, null, null, false),
    (305, 11, 'IMAGE', 'interests.images.0.image', null, 101, '1', null, false),
    (306, 11, 'IMAGE', 'interests.images.1.image', null, 102, '2', null, false),
    (308, 11, 'TEXT', 'indicators.title', 'Indicateurs', null, null, null, false),
    (309, 11, 'TEXT', 'indicators.indicators.0.name', 'Espèces inventoriées à Saint-Pierre-Et-Miquelon', null, null, null, false),
    (310, 11, 'TEXT', 'indicators.indicators.0.value', '2083', null, null, null, false),
    (311, 11, 'IMAGE', 'indicators.indicators.0.image', null, 103, 'Espèces inventoriées à Saint-Pierre-Et-Miquelon', null, false),
    (312, 11, 'LINK', 'indicators.indicators.0.link', 'Voir les espèces inventoriées ', null, null, '/indicators/especes-inventoriee-st-pierre-et-miquelon', false),
    (313, 11, 'TEXT', 'species.title', 'Espèces', null, null, null, false),
    (314, 11, 'TEXT', 'species.species.0.name', 'Cerf de Virginie', null, null, null, false),
    (315, 11, 'TEXT', 'species.species.0.description', 'Peu de mammifères terrestres...', null, null, null, false),
    (316, 11, 'IMAGE', 'species.species.0.image', null, 104, 'Cerf de Virginie', null, false),
    (317, 11, 'TEXT', 'ecosystems.title', 'Écosystèmes', null, null, null, false),
    (318, 11, 'TEXT', 'ecosystems.ecosystems.0.name', 'Les marais tourbeux', null, null, null, false),
    (319, 11, 'TEXT', 'ecosystems.ecosystems.0.description', 'Ces zones humides...', null, null, null, false),
    (320, 11, 'IMAGE', 'ecosystems.ecosystems.0.image', null, 108, 'Tourbières', null, false),
    (321, 11, 'TEXT', 'timeline.title', 'Frise chronologique', null, null, null, false),
    (322, 11, 'TEXT', 'timeline.events.0.name', '1535', null, null, null, false),
    (323, 11, 'TEXT', 'timeline.events.0.description', 'Prise de possession française...', null, null, null, false),
    (324, 11, 'TEXT', 'timeline.events.1.name', '17ème siècle', null, null, null, false),
    (325, 11, 'TEXT', 'timeline.events.1.description', 'Premières installations permanentes...', null, null, null, false),
    (326, 11, 'TEXT', 'risks.title', 'Risques et enjeux', null, null, null, false),
    (327, 11, 'TEXT', 'risks.risks.0.name', 'Les ressources naturelles', null, null, null, false),
    (328, 11, 'TEXT', 'risks.risks.0.description', 'Le territoire est pour l''instant...', null, null, null, false),
    (329, 11, 'IMAGE', 'risks.risks.0.image', null, 109, 'Ressources naturelles', null, false),
    (330, 11, 'IMAGE', 'other.image', null, 105, 'Autre territoire', null, false),
    (331, 11, 'LINK', 'other.link', 'Portail local de l''environnement', null, null, 'https://oeil.nc', false),

--     Indicator home
    (400, 29, 'TEXT', 'title', 'Compter la biodiversité, oui mais comment ?', null, null, null, true),
    (401, 29, 'TEXT', 'presentation', 'Lorem ipsum dolor', null, null, null, false),
    (402, 29, 'IMAGE', 'image', null, 40, 'Indicateurs', null, false),

--     Indicator
--     Espèces envahissantes
    (500, 30, 'TEXT', 'name',  'Espèces envahissantes', null, null, null, true),
    (503, 30, 'TEXT', 'presentation.description',  'espèces sur les 100...', null, null, null, false),
    (504, 30, 'IMAGE', 'presentation.image',  null, 104, 'Espèces envahissantes', null, false),
    (505, 30, 'TEXT', 'understand.title',  'Comprendre', null, null, null, false),
    (506, 30, 'IMAGE', 'understand.image',  null, 104, 'Espèces envahissantes', null, false),
    (507, 30, 'TEXT', 'understand.sections.0.title',  'Raison 1', null, null, null, false),
    (508, 30, 'TEXT', 'understand.sections.0.description',  'Explication raison 1', null, null, null, false),
    (509, 30, 'TEXT', 'understand.sections.1.title',  'Raison 2', null, null, null, false),
    (510, 30, 'TEXT', 'understand.sections.1.description',  'Explication raison 2', null, null, null, false),
    (511, 30, 'TEXT', 'indicators.title',  'Espèces envahissantes par territoire', null, null, null, false),
    (518, 30, 'TEXT', 'ecogestures.title', 'Écogestes', null, null, null, false),
    (519, 30, 'TEXT', 'ecogestures.ecogestures.0.name', 'Protégeons les récifs corallien', null, null, null, false),
    (520, 30, 'TEXT', 'ecogestures.ecogestures.0.category', 'Loisirs', null, null, null, false),
    (521, 30, 'TEXT', 'ecogestures.ecogestures.0.description', 'Protégeons les récifs corallien...', null, null, null, false),
    (522, 30, 'LINK', 'ecogestures.ecogestures.0.link', 'Protégeons les récifs corallien', null, null, '/ecogestes/recifs', false),
    (523, 30, 'IMAGE', 'ecogestures.ecogestures.0.image', null, 104, 'Tortue', null, false),
    (524, 30, 'TEXT', 'next.name', 'Surfaces des forêts', null, null, null, false),
    (525, 30, 'IMAGE', 'next.image', null, 105, 'Surfaces des forêts', null, false),
    (526, 30, 'LINK', 'next.link', 'Surfaces des forêts', null, null, '/indicateurs/surface-forêts', false);


commit;
