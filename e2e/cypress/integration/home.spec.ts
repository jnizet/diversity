describe('Home', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Découvrez la biodiversité des Outre-Mer');
    cy.get('footer').should('contain', 'Territoires');
  });
});
