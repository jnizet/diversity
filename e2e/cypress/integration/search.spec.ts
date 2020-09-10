describe('Search', () => {
  it('should have a header, title and footer, display search results, and navigate to found page', () => {
    cy.visit('/');
    cy.get('#search').type('compteur biodiversité').type('{enter}');

    cy.get('header').should('contain', 'Layout header');
    cy.get('h1').should('contain', 'Résultats de la recherche');
    cy.get('footer').should('contain', 'Layout footer');
    cy.get('main').contains('À propos').click();

    cy.get('h1').should('contain', 'Pourquoi un compteur de la biodiversité en outre-mer ?');
  });
});
