describe('About', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('À propos').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Pourquoi un compteur de la biodiversité en outre-mer ?');
    cy.get('footer').should('contain', 'Territoires');
  });
});
