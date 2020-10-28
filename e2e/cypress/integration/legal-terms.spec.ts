describe('Legal terms', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Mentions légales').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Mentions légales');
    cy.get('footer').should('contain', 'Territoires');
  });
});
