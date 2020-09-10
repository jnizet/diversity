describe('Indicator', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/indicateurs/especes-envahissantes');

    cy.get('header').should('contain', 'Layout header');
    cy.get('h1').should('contain', 'Espèces envahissantes');
    cy.get('footer').should('contain', 'Layout footer');
  });
});
