describe('Error page', () => {
  it('should have a header, title content and footer', () => {
    cy.visit('/this-does-not-exist', { failOnStatusCode: false });

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', '404');
    cy.get('main').should('contain', 'La page que vous avez demandée semble introuvable');
    cy.get('footer').should('contain', 'Territoires');
  });
});
