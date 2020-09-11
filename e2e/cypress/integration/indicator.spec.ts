describe('Indicator', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/indicateurs/especes-envahissantes');

    cy.get('header').should('contain', 'Layout header');
    cy.get('h1').should('contain', 'Espèces envahissantes');
    cy.get('.indicators h3').should('contain', 'Réunion');
    cy.get('.indicators p').should('contain', '6');
    cy.get('.indicators h3').should('contain', 'Guadeloupe');
    cy.get('.indicators p').should('contain', '14');
    cy.get('footer').should('contain', 'Layout footer');
  });
});
