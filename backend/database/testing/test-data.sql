delete from page_element;
delete from page;
delete from image;
delete from indicator_value;
delete from indicator;

insert into indicator (id, biom_id) values
    (1, 'indicator1');

insert into indicator_value (id, indicator_id, territory, value, unit) values
    (1, 1, 'REUNION', 23.4, 'km');


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

insert into page (id, name, model_name) values
   (1, 'Home', 'home'),
   (2, 'About', 'about'),
   (3, 'recifs', 'ecogesture'),
   (4, 'EcoGestureHome', 'ecogestures'),
   (10, 'reunion', 'territory'),
   (11, 'st-pierre-et-miquelon', 'territory'),
   (30, 'especes-envahissantes', 'indicator');

insert into page_element (id, page_id, type, key, text, image_id, alt, href) values
--     Home
    (11, 1, 'TEXT', 'carousel.title', 'Ensemble protégeons la diversité des Outre-Mer', null, null, null),
    (12, 1, 'TEXT', 'carousel.text', 'Partez à la rencontre des espèces et des écosystèmes des territoires français d''outre-mer', null, null, null),
    (13, 1, 'IMAGE', 'carousel.images.0.image', null, 1, '1', null),
    (14, 1, 'IMAGE', 'carousel.images.1.image', null, 2, '2', null),
    (15, 1, 'IMAGE', 'carousel.images.2.image', null, 3, '3', null),
    (16, 1, 'TEXT', 'carousel.territoriesButton', 'Découvrir les territoires', null, null, null),
    (17, 1, 'TEXT', 'presentation.title', 'Le compteur de biodiversité Outre-Mer', null, null, null),
    (18, 1, 'TEXT', 'presentation.text', 'Les territoires d''outre-mer présentent une biodiversité particulièrement riche et variée, ...', null, null, null),
    (19, 1, 'IMAGE', 'presentation.image', null, 4, 'À propos', null),
    (20, 1, 'TEXT', 'presentation.more', 'En savoir plus', null, null, null),
    (21, 1, 'TEXT', 'indicators.title', 'Une biodiversité unique et fragile, protégeons-la !', null, null, null),
    (22, 1, 'TEXT', 'indicators.text', 'La conservation de la biodiversité dépend de sa connaissance ...', null, null, null),
    (23, 1, 'IMAGE', 'indicators.image', null, 5, 'Indicateurs', null),
    (24, 1, 'TEXT', 'science.title', 'Du « super-corail » pour sauver les récifs', null, null, null),
    (25, 1, 'TEXT', 'science.text', 'Au sein de l''archipel des Seychelles le premier projet de restauration des récifs coralliens à grande échelle a débuté.', null, null, null),
    (26, 1, 'IMAGE', 'science.image', null, 6, 'Science participative', null),

--     About
    (41, 2, 'TEXT', 'header.title', 'Pourquoi un compteur de la biodiversité en outre-mer ?', null, null, null),
    (42, 2, 'TEXT', 'header.subtitle', 'Partager la connaissance et encourager chacun...', null, null, null),
    (43, 2, 'IMAGE', 'header.background', null, 7, 'Fond', null),
    (44, 2, 'TEXT', 'header.paragraphs.0.text', 'Les territoires d''outre-mer présentent...', null, null, null),
    (45, 2, 'TEXT', 'header.paragraphs.1.text', 'La mission du compteur est de...', null, null, null),
    (46, 2, 'TEXT', 'carousel.0.title', 'Partager la connaissance scientifique', null, null, null),
    (47, 2, 'TEXT', 'carousel.0.text', 'Quelles sont les espèces présentes sur ce territoire...', null, null, null),
    (48, 2, 'IMAGE', 'carousel.0.image', null, 1, '1', null),
    (49, 2, 'LINK', 'carousel.0.link', 'Voir tous les indicateurs', null, null, '/indicators'),
    (50, 2, 'TEXT', 'partners.title', 'Ils contribuent au compteur de la biodiversité', null, null, null),
    (51, 2, 'IMAGE', 'partners.partners.0.logo', null, 8, 'Logo1', null),
    (52, 2, 'IMAGE', 'partners.partners.1.logo', null, 9, 'Logo2', null),
    (53, 2, 'IMAGE', 'partners.partners.2.logo', null, 10, 'Logo3', null),

--     Ecogesture 1
    (81, 3, 'TEXT', 'presentation.name', 'Protégeons les récifs coralliens', null, null, null),
    (82, 3, 'TEXT', 'presentation.category', 'Loisirs', null, null, null),
    (83, 3, 'TEXT', 'presentation.description', 'Sinon ils vont mourir', null, null, null),
    (84, 3, 'IMAGE', 'presentation.image', null, 30, 'Jolis coraux', null),
    (85, 3, 'IMAGE', 'presentation.file', null, 31, 'Fiche technique', null),
    (86, 3, 'TEXT', 'understand.title', 'Comprendre : un écosystème très riche', null, null, null),
    (87, 3, 'TEXT', 'understand.text', 'Les récifs coralliens affichent plus d''un tiers des espèces marines connues...', null, null, null),
    (88, 3, 'IMAGE', 'understand.image', null, 32, 'Comprendre', null),
    (89, 3, 'TEXT', 'action.title', 'Les bons gestes pour protéger les récifs', null, null, null),
    (90, 3, 'IMAGE', 'action.cards.0.icon', null, 33, 'Crème solaire', null),
    (91, 3, 'TEXT', 'action.cards.0.description', 'Je choisis une crème solaire non nocive pour l''environnement', null, null, null),
    (92, 3, 'IMAGE', 'action.cards.1.icon', null, 34, 'Bateau', null),
    (93, 3, 'TEXT', 'action.cards.1.description', 'En bâteau, je ne jette pas l''ancre à proximité de récifs', null, null, null),

--     Ecogestures home
    (161, 4, 'TEXT', 'title', 'Réinventons notre façon de vivre et de voyager grâce aux écogestes', null, null, null),
    (162, 4, 'TEXT', 'presentation', 'Lorem ipsum dolor', null, null, null),
    (163, 4, 'IMAGE', 'image', null, 91, 'Ecogestes', null),

--     Territory
--     Reunion
    (200, 10, 'TEXT', 'name',  'La Réunion', null, null, null),
    (201, 10, 'TEXT', 'identity.title',  'Un climat tropical', null, null, null),
    (202, 10, 'TEXT', 'identity.presentation',  'À l''ouest de l''Océan Indien...', null, null, null),
    (203, 10, 'IMAGE', 'identity.infography', null, 106, 'Infographie Réunion', null),
    (204, 10, 'TEXT', 'interests.title', 'À la découverte d''endroits emblématiques', null, null, null),
    (205, 10, 'IMAGE', 'interests.images.0.image', null, 101, '1', null),
    (206, 10, 'IMAGE', 'interests.images.1.image', null, 102, '2', null),
    (207, 10, 'TEXT', 'indicators.title', 'Indicateurs', null, null, null),
    (208, 10, 'TEXT', 'indicators.indicators.0.name', 'Espèces inventoriées à la Réunion', null, null, null),
    (209, 10, 'TEXT', 'indicators.indicators.0.value', '4123', null, null, null),
    (210, 10, 'IMAGE', 'indicators.indicators.0.image', null, 103, 'Espèces inventoriées à la Réunion', null),
    (211, 10, 'LINK', 'indicators.indicators.0.link', 'Voir les espèces inventoriées ', null, null, '/indicators/especes-inventoriee-la-reunion'),
    (212, 10, 'TEXT', 'indicators.indicators.1.name', 'Surface des forêts', null, null, null),
    (213, 10, 'TEXT', 'indicators.indicators.1.value', '219000', null, null, null),
    (214, 10, 'IMAGE', 'indicators.indicators.1.image', null, 103, 'Surface des forêts', null),
    (215, 10, 'LINK', 'indicators.indicators.1.link', 'Voir les forêts ', null, null, '/indicators/forêts-la-reunion'),
    (216, 10, 'TEXT', 'species.title', 'Espèces', null, null, null),
    (217, 10, 'TEXT', 'species.species.0.name', 'Papangue', null, null, null),
    (218, 10, 'TEXT', 'species.species.0.description', 'Dernier rapace de la Réunion', null, null, null),
    (219, 10, 'IMAGE', 'species.species.0.image', null, 104, 'Papangue', null),
    (220, 10, 'TEXT', 'ecosystems.title', 'Écosystèmes', null, null, null),
    (221, 10, 'TEXT', 'timeline.title', 'Frise chronologique', null, null, null),
    (222, 10, 'TEXT', 'risks.title', 'Risques et enjeux', null, null, null),
    (223, 10, 'IMAGE', 'other.image', null, 105, 'Autre territoire', null),
    (224, 10, 'LINK', 'other.link', 'Portail local de l''environnement', null, null, 'https://oeil.nc'),

--     St-Pierre-Et-Miquelon
    (300, 11, 'TEXT', 'name',  'Saint-Pierre-et-Miquelon', null, null, null),
    (301, 11, 'TEXT', 'identity.title',  'Un climat subarctique, froid et humide', null, null, null),
    (302, 11, 'TEXT', 'identity.presentation',  'Au sud de Terre-Neuve (Canada)...', null, null, null),
    (303, 11, 'IMAGE', 'identity.infography', null, 107, 'Infographie Saint-Pierre-et-Miquelon', null),
    (304, 11, 'TEXT', 'interests.title', 'À la découverte d''endroits emblématiques', null, null, null),
    (305, 11, 'IMAGE', 'interests.images.0.image', null, 101, '1', null),
    (306, 11, 'IMAGE', 'interests.images.1.image', null, 102, '2', null),
    (308, 11, 'TEXT', 'indicators.title', 'Indicateurs', null, null, null),
    (309, 11, 'TEXT', 'indicators.indicators.0.name', 'Espèces inventoriées à Saint-Pierre-Et-Miquelon', null, null, null),
    (310, 11, 'TEXT', 'indicators.indicators.0.value', '2083', null, null, null),
    (311, 11, 'IMAGE', 'indicators.indicators.0.image', null, 103, 'Espèces inventoriées à Saint-Pierre-Et-Miquelon', null),
    (312, 11, 'LINK', 'indicators.indicators.0.link', 'Voir les espèces inventoriées ', null, null, '/indicators/especes-inventoriee-st-pierre-et-miquelon'),
    (313, 11, 'TEXT', 'species.title', 'Espèces', null, null, null),
    (314, 11, 'TEXT', 'species.species.0.name', 'Cerf de Virginie', null, null, null),
    (315, 11, 'TEXT', 'species.species.0.description', 'Peu de mammifères terrestres...', null, null, null),
    (316, 11, 'IMAGE', 'species.species.0.image', null, 104, 'Cerf de Virginie', null),
    (317, 11, 'TEXT', 'ecosystems.title', 'Écosystèmes', null, null, null),
    (318, 11, 'TEXT', 'ecosystems.ecosystems.0.name', 'Les marais tourbeux', null, null, null),
    (319, 11, 'TEXT', 'ecosystems.ecosystems.0.description', 'Ces zones humides...', null, null, null),
    (320, 11, 'IMAGE', 'ecosystems.ecosystems.0.image', null, 108, 'Tourbières', null),
    (321, 11, 'TEXT', 'timeline.title', 'Frise chronologique', null, null, null),
    (322, 11, 'TEXT', 'timeline.events.0.name', '1535', null, null, null),
    (323, 11, 'TEXT', 'timeline.events.0.description', 'Prise de possession française...', null, null, null),
    (324, 11, 'TEXT', 'timeline.events.1.name', '17ème siècle', null, null, null),
    (325, 11, 'TEXT', 'timeline.events.1.description', 'Premières installations permanentes...', null, null, null),
    (326, 11, 'TEXT', 'risks.title', 'Risques et enjeux', null, null, null),
    (327, 11, 'TEXT', 'risks.risks.0.name', 'Les ressources naturelles', null, null, null),
    (328, 11, 'TEXT', 'risks.risks.0.description', 'Le territoire est pour l''instant...', null, null, null),
    (329, 11, 'IMAGE', 'risks.risks.0.image', null, 109, 'Ressources naturelles', null),
    (330, 11, 'IMAGE', 'other.image', null, 105, 'Autre territoire', null),
    (331, 11, 'LINK', 'other.link', 'Portail local de l''environnement', null, null, 'https://oeil.nc'),

--     Indicator
--     Espèces envahissantes
    (500, 30, 'TEXT', 'name',  'Espèces envahissantes', null, null, null),
    (501, 30, 'TEXT', 'presentation.category',  'Espèces', null, null, null),
    (502, 30, 'TEXT', 'indicator.OUTRE_MER.value',  '14', null, null, null),
    (503, 30, 'TEXT', 'presentation.description',  'espèces sur les 100...', null, null, null),
    (504, 30, 'IMAGE', 'presentation.image',  null, 104, 'Espèces envahissantes', null),
    (505, 30, 'TEXT', 'understand.title',  'Comprendre', null, null, null),
    (506, 30, 'IMAGE', 'understand.image',  null, 104, 'Espèces envahissantes', null),
    (507, 30, 'TEXT', 'understand.sections.0.title',  'Raison 1', null, null, null),
    (508, 30, 'TEXT', 'understand.sections.0.description',  'Explication raison 1', null, null, null),
    (509, 30, 'TEXT', 'understand.sections.1.title',  'Raison 2', null, null, null),
    (510, 30, 'TEXT', 'understand.sections.1.description',  'Explication raison 2', null, null, null),
    (511, 30, 'TEXT', 'indicators.title',  'Espèces envahissantes par territoire', null, null, null),
    (512, 30, 'TEXT', 'indicator.REUNION.value',  '6', null, null, null),
    (513, 30, 'TEXT', 'indicator.GUADELOUPE.value',  '60', null, null, null),
    (514, 30, 'TEXT', 'indicator.SAINT_PIERRE_ET_MIQUELON.value',  '24', null, null, null),
    (518, 30, 'TEXT', 'ecogestures.title', 'Écogestes', null, null, null),
    (519, 30, 'TEXT', 'ecogestures.ecogestures.0.name', 'Protégeons les récifs corallien', null, null, null),
    (520, 30, 'TEXT', 'ecogestures.ecogestures.0.category', 'Loisirs', null, null, null),
    (521, 30, 'TEXT', 'ecogestures.ecogestures.0.description', 'Protégeons les récifs corallien...', null, null, null),
    (522, 30, 'LINK', 'ecogestures.ecogestures.0.link', 'Protégeons les récifs corallien', null, null, '/ecogestes/recifs'),
    (523, 30, 'IMAGE', 'ecogestures.ecogestures.0.image', null, 104, 'Tortue', null),
    (524, 30, 'TEXT', 'next.name', 'Surfaces des forêts', null, null, null),
    (525, 30, 'IMAGE', 'next.image', null, 105, 'Surfaces des forêts', null),
    (526, 30, 'LINK', 'next.link', 'Surfaces des forêts', null, null, '/indicateurs/surface-forêts');

commit;
