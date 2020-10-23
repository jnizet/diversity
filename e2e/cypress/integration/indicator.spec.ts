describe('Indicator', () => {
  it('should have a header, title values by territory, ecogestures and footer', () => {
    cy.visit('/indicateurs/especes-envahissantes');

    cy.get('header').should('contain', 'Territoires');

    cy.get('h1').should('contain', '64');
    cy.get('.indicateur-slide .indicateur-slide-tiytle').should('contain', 'Réunion');
    cy.get('.indicateur-slide .indicateur-slide-number').should('contain', '40');
    cy.get('.indicateur-slide .indicateur-slide-tiytle').should('contain', 'Guadeloupe');
    cy.get('.indicateur-slide .indicateur-slide-number').should('contain', '14');
    cy.get('h3.ecogeste-link-title').should('contain', 'Protégeons les récifs coralliens');
    cy.get('.push-next-wrapper').should('contain', '5\u00a0%').should('contain', 'de la forêt disparaît');
    cy.get('footer').should('contain', 'Territoires');
  });

  it('should navigate to next indicator', () => {
    cy.visit('/indicateurs/especes-envahissantes');
    cy.get('.push-next-wrapper').click({ force: true });
    cy.title().should('eq', 'Déforestation');
  });

  it('should navigate to ecogesture', () => {
    cy.visit('/indicateurs/especes-envahissantes');
    cy.get('.ecogeste-link-title').click({ force: true });
    cy.title().should('eq', 'Écogeste: protéger les récifs');
  });
});
