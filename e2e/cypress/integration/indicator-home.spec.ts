describe('Indicator home', () => {
  it('should have a header, title, indicators and footer', () => {
    cy.visit('/');
    cy.contains('Indicateurs').click();

    cy.get('header').should('contain', 'Layout header');
    cy.get('h1').should('contain', 'Compter la biodiversité, oui mais comment ?');
    cy.get('#categories').should('contain', 'Tout');
    cy.get('#categories').should('contain', 'Écosystèmes');
    cy.get('#indicators').should('contain', '64');
    cy.get('#indicators').should('contain', 'espèces sur les 100');
    cy.get('footer').should('contain', 'Layout footer');
  });

  it('should link to indicator', () => {
    cy.visit('/indicateurs');

    cy.contains('En savoir plus').click();
    cy.get('h1').should('contain', 'Espèces envahissantes');
  });
});
