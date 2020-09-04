delete from page_element;
delete from page;

insert into page (id, name, model_name) values (1, 'Home', 'home');

insert into page_element (id, page_id, type, key, text, image_id, alt, href) values
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
    (26, 1, 'IMAGE', 'science.image', null, '600x400?text=science+participative', 'Science participative', null)

commit;
