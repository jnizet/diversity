delete from page_element;
delete from page;
delete from image;
delete from territory;

insert into territory (id, name, slug) values
    (1, 'Réunion', 'reunion'),
    (2, 'Saint-Pierre-Et-Miquelon', 'st-pierre-et-miquelon');

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
    (34, 'image/png', 'vignette2.png')

insert into page (id, name, model_name) values
   (1, 'Home', 'home'),
   (2, 'About', 'about'),
   (3, 'recifs', 'ecogesture');

insert into page_element (id, page_id, type, key, text, image_id, alt, href) values
--     Home
    (11, 1, 'TEXT', 'carousel.title', 'Ensemble protégeons la diversité des Outre-Mer', null, null, null),
    (12, 1, 'TEXT', 'carousel.text', 'Partez à la rencontre des espèces et des écosystèmes des territoires français d''outre-mer', null, null, null),
    (13, 1, 'IMAGE', 'carousel.images.0.image', null, 1, '1', null),
    (14, 1, 'IMAGE', 'carousel.images.1.image', null, 2, '2', null),
    (15, 1, 'IMAGE', 'carousel.images.2.image', null, 3, '3', null),
    (16, 1, 'TEXT', 'carousel.territoriesButton', 'Découvrir les territoires', null, null, null),
    (17, 1, 'TEXT', 'presentation.title', 'Le compteur de biodiversité Outre-Mer', null, null, null),
    (18, 1, 'TEXT', 'presentation.text', 'Les territoires d''outre-mer présentent une biodiversité perticulièrement riche et variée, ...', null, null, null),
    (19, 1, 'IMAGE', 'presentation.image', null, 4, 'À propos', null),
    (20, 1, 'TEXT', 'presentation.more', 'En savoir plus', null, null, null),
    (21, 1, 'TEXT', 'indicators.title', 'Une biodiversité unique et fragile, protégons-la !', null, null, null),
    (22, 1, 'TEXT', 'indicators.text', 'La concervation de la biodiversité dépend de sa connaissance ...', null, null, null),
    (23, 1, 'IMAGE', 'indicators.image', null, 5, 'Indicateurs', null),
    (24, 1, 'TEXT', 'science.title', 'Du « super-corail » pour sauver les récifs', null, null, null),
    (25, 1, 'TEXT', 'science.text', 'Au sein de l''archipel des Seychelles le premier projet de restauration des récifs coraliens à grande échelle a débuté.', null, null, null),
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
    (93, 3, 'TEXT', 'action.cards.1.description', 'En bâteau, je ne jette pas l''ancre à proximité de récifs', null, null, null);

commit;
