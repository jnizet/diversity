describe('Territory', () => {
  it('should have a page for La Réunion', () => {
    cy.visit('/territoires/reunion');

    cy.get('header').should('contain', 'Territoires');

    // title
    cy.get('h1').should('contain', 'La Réunion, entre mer et montagnes');

    // identity
    cy.get('h2').should('contain', 'Un climat tropical');
    cy.get('p').should('contain', "À l'ouest de l'Océan Indien");

    // interests
    cy.get('.lieu-slide').should('have.length', 2);

    // species
    cy.get('.espece-slide h3').should('contain', 'Papangue');
    cy.get('.espece-slide p').should('contain', 'Dernier rapace de la Réunion');

    // timeline
    cy.get('.frise-element .frise-element-title').should('contain', '1535');
    cy.get('.frise-element p').should('contain', 'Prise de possession française');

    // ecosystems
    cy.get('.ecosysteme-slide h3').should('contain', 'Les marais tourbeux et les tourbières');
    cy.get('.ecosysteme-slide p').should('contain', 'Ces zones humides occupent des superficies importantes');

    // risks
    cy.get('.risk-element h3').should('contain', 'Les ressources naturelles');
    cy.get('.risk-element p').should('contain', 'Le territoire est pour le moment le seul');

    // other territory
    cy.get('a.section-push-next').should('have.attr', 'href', '/territoires/saint-pierre-et-miquelon');

    cy.get('footer').should('contain', 'Territoires');
  });

  it('should navigate to Saint-Pierre-Et-Miquelon', () => {
    cy.visit('/territoires/reunion');

    cy.get('a.section-push-next').click();

    cy.get('h1').should('contain', 'St-Pierre-et-Miquelon, des eaux très riches en biodiversité');
  });

  it('should navigate to first indicator', () => {
    cy.visit('/territoires/reunion');

    cy.get('.indicator-card').contains('65').closest('.indicator-card').contains('En savoir plus').click();

    cy.get('h1').should('contain', '65');
  });

  it('should navigate to second indicator', () => {
    cy.visit('/territoires/reunion');

    cy.get('.indicator-card').contains('de la forêt').closest('.indicator-card').contains('En savoir plus').click();

    cy.get('h1').should('contain', 'de la forêt');
  });
});
