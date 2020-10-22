describe('Science', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/sciences-participatives');

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Que sont les sciences participatives ?');
    cy.get('footer').should('contain', 'Territoires');
  });
});
