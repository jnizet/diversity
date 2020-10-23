describe('Territory home', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Territoires').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'A la découverte des Outre-mer');
    cy.contains('6 274 habitants');
    cy.contains('85 117 espèces indigènes');
    cy.get('footer').should('contain', 'Territoires');
  });
});
