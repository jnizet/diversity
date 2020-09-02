describe('Home', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');

    cy.get('header').should('contain', 'Layout header');
    cy.get('h1').should('contain', 'Hello');
    cy.get('footer').should('contain', 'Layout footer');
  });
});
