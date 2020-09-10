describe('Ecogesture', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Écogestes').click();

    cy.get('header').should('contain', 'Layout header');
    cy.get('h1').should('contain', 'Réinventons notre façon de vivre et de voyager grâce aux écogestes');
    cy.get('footer').should('contain', 'Layout footer');
  });
});
