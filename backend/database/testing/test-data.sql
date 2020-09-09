delete from page_element;
delete from page;

insert into page (id, name, model_name) values (1, 'Home', 'home');
insert into page (id, name, model_name) values (2, 'About', 'about');

insert into page_element (id, page_id, type, key, text, image_id, alt, href) values
--     Home
    (11, 1, 'TEXT', 'carousel.title', 'Ensemble protégeons la diversité des Outre-Mer', null, null, null),
    (12, 1, 'TEXT', 'carousel.text', 'Partez à la rencontre des espèces et des écosystèmes des territoires français d''outre-mer', null, null, null),
    (13, 1, 'IMAGE', 'carousel.images.0.image', null, '600x400?text=carousel+1', '1', null),
    (14, 1, 'IMAGE', 'carousel.images.1.image', null, '600x400?text=carousel+2', '2', null),
    (15, 1, 'IMAGE', 'carousel.images.2.image', null, '600x400?text=carousel+3', '3', null),
    (16, 1, 'TEXT', 'carousel.territoriesButton', 'Découvrir les territoires', null, null, null),
    (17, 1, 'TEXT', 'presentation.title', 'Le compteur de biodiversité Outre-Mer', null, null, null),
    (18, 1, 'TEXT', 'presentation.text', 'Les territoires d''outre-mer présentent une biodiversité perticulièrement riche et variée, ...', null, null, null),
    (19, 1, 'IMAGE', 'presentation.image', null, '600x400?text=a+propos', 'À propos', null),
    (20, 1, 'TEXT', 'presentation.more', 'En savoir plus', null, null, null),
    (21, 1, 'TEXT', 'indicators.title', 'Une biodiversité unique et fragile, protégons-la !', null, null, null),
    (22, 1, 'TEXT', 'indicators.text', 'La concervation de la biodiversité dépend de sa connaissance ...', null, null, null),
    (23, 1, 'IMAGE', 'indicators.image', null, '600x400?text=indicateurs', 'Indicateurs', null),
    (24, 1, 'TEXT', 'science.title', 'Du « super-corail » pour sauver les récifs', null, null, null),
    (25, 1, 'TEXT', 'science.text', 'Au sein de l''archipel des Seychelles le premier projet de restauration des récifs coraliens à grande échelle a débuté.', null, null, null),
    (26, 1, 'IMAGE', 'science.image', null, '600x400?text=science+participative', 'Science participative', null),

--     About
    (41, 2, 'TEXT', 'header.title', 'Pourquoi un compteur de la biodiversité en outre-mer ?', null, null, null),
    (42, 2, 'TEXT', 'header.subtitle', 'Partager la connaissance et encourager chacun...', null, null, null),
    (43, 2, 'IMAGE', 'header.background', null, '600x400?text=fond', 'Fond', null),
    (44, 2, 'TEXT', 'header.paragraphs.0.text', 'Les territoires d''outre-mer présentent...', null, null, null),
    (45, 2, 'TEXT', 'header.paragraphs.1.text', 'La mission du compteur est de...', null, null, null),
    (46, 2, 'TEXT', 'carousel.0.title', 'Partager la connaissance scientifique', null, null, null),
    (47, 2, 'TEXT', 'carousel.0.text', 'Quelles sont les espèces présentes sur ce territoire...', null, null, null),
    (48, 2, 'IMAGE', 'carousel.0.image', null, '600x400?text=carousel+1', '1', null),
    (49, 2, 'LINK', 'carousel.0.link', 'Voir tous les indicateurs', null, null, '/indicators'),
    (50, 2, 'TEXT', 'partners.title', 'Ils contribuent au compteur de la biodiversité', null, null, null),
    (51, 2, 'IMAGE', 'partners.partners.0.logo', null, '50?text=logo1', 'Logo1', null),
    (52, 2, 'IMAGE', 'partners.partners.1.logo', null, '50?text=logo2', 'Logo2', null),
    (53, 2, 'IMAGE', 'partners.partners.2.logo', null, '50?text=logo3', 'Logo3', null);

commit;
