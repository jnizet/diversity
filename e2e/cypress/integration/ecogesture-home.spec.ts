describe('Ecogesture home', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Éco-gestes').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Réinventons notre façon de vivre et de voyager grâce aux écogestes');
    cy.get('footer').should('contain', 'Territoires');
  });
});
