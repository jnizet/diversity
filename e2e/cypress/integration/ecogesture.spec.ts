describe('Ecogesture', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Écogestes').click();
    cy.get('a.ecogesture-link').click();

    cy.get('header').should('contain', 'Layout header');
    cy.get('h1').should('contain', 'Protégeons les récifs coralliens');
    cy.get('footer').should('contain', 'Layout footer');
  });
});