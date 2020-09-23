describe('Indicator', () => {
  it('should have a header, title values by territory, ecogestures and footer', () => {
    cy.visit('/indicateurs/especes-envahissantes');

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Espèces envahissantes');
    cy.get('.indicators h3').should('contain', 'Réunion');
    cy.get('.indicators p').should('contain', '40');
    cy.get('.indicators h3').should('contain', 'Guadeloupe');
    cy.get('.indicators p').should('contain', '14');
    cy.get('.ecogestures h3').should('contain', 'Protégeons les récifs coralliens');
    cy.get('footer').should('contain', 'Territoires');
  });
});
