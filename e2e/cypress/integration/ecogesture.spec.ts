describe('Ecogesture', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Éco-gestes').click();
    cy.get('a.ecogesture-link').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Protégeons les récifs coralliens');
    cy.get('footer').should('contain', 'Territoires');
  });
});
