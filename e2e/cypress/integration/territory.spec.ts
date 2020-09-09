describe('Territory', () => {
  it('should have a page for La Réunion', () => {
    cy.visit('/territoires/reunion');

    cy.get('header').should('contain', 'Layout header');

    // title
    cy.get('h1').should('contain', 'La Réunion');

    // identity
    cy.get('.identity h2').should('contain', 'Un climat tropical');
    cy.get('.identity p').should('contain', "À l'ouest de l'Océan Indien");
    cy.get('.identity img').should('have.attr', 'src', '/images/106/image');

    // interests
    cy.get('.interests img').should('have.length', 2);

    // indicators
    cy.get('.indicators h3').should('contain', 'Espèces inventoriées');
    cy.get('.indicators p').should('contain', '4123');
    cy.get('.indicators img').should('have.attr', 'src', '/images/103/image');
    cy.get('.indicators a').should('have.attr', 'href', '/indicators/especes');

    // species
    cy.get('.species h3').should('contain', 'Papangue');
    cy.get('.species p').should('contain', 'Dernier rapace de la Réunion');
    cy.get('.species img').should('have.attr', 'src', '/images/104/image');

    cy.get('footer').should('contain', 'Layout footer');
  });

  it('should have a page for Saint-Pierre-Et-Miquelon', () => {
    cy.visit('/territoires/st-pierre-et-miquelon');

    // title
    cy.get('h1').should('contain', 'Saint-Pierre-Et-Miquelon');

    // identity
    cy.get('.identity h2').should('contain', 'Un climat subarctique, froid et humide');
    cy.get('.identity p').should('contain', 'Au sud de Terre-Neuve (Canada)');
    cy.get('.identity img').should('have.attr', 'src', '/images/107/image');

    // interests
    cy.get('.interests img').should('have.length', 1);

    // ecosystems
    cy.get('.ecosystems h3').should('contain', 'Les marais tourbeux');
    cy.get('.ecosystems p').should('contain', 'Ces zones humides...');
    cy.get('.ecosystems img').should('have.attr', 'src', '/images/108/image');

    // timeline
    cy.get('.timeline h3').should('contain', '1535');
    cy.get('.timeline p').should('contain', 'Prise de possession française...');
    cy.get('.timeline h3').should('contain', '17ème siècle');
    cy.get('.timeline p').should('contain', 'Premières installations permanentes...');

    // risks
    cy.get('.risks h3').should('contain', 'Les ressources naturelles');
    cy.get('.risks p').should('contain', "Le territoire est pour l'instant...");
    cy.get('.risks img').should('have.attr', 'src', '/images/109/image');

    // other territory
    cy.get('.other-territory img').should('have.attr', 'src', '/images/105/image');
    cy.get('.other-territory a').should('have.attr', 'href', 'https://oeil.nc');
  });
});
