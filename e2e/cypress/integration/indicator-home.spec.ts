describe('Indicator home', () => {
  it('should have a header, title, indicators and footer', () => {
    cy.visit('/');
    cy.contains('Indicateurs').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Compter la biodiversité, oui mais comment ?');
    cy.get('#categories').should('contain', 'Tout');
    cy.get('#categories').should('contain', 'Écosystèmes');
    cy.get('#indicators').should('contain', '64');
    cy.get('#indicators').should('contain', 'espèces sur les 100');
    cy.get('footer').should('contain', 'Territoires');
  });

  it('should link to indicator', () => {
    cy.visit('/indicateurs');

    cy.contains('En savoir plus').click({ force: true });
    cy.get('h1').should('contain', 'Espèces envahissantes');
  });

  it('should filter indicators by categories', () => {
    cy.visit('/indicateurs');

    // initially, "All" is selected, categories are not deselected, and all indicators are visible
    cy.contains('Tout').should('have.class', 'selected');
    cy.contains('Écosystèmes').should('not.have.class', 'selected');
    cy.contains('Végétation').should('not.have.class', 'selected');
    cy.contains('espèces sur les 100').should('be.visible');
    cy.contains('de la forêt disparaît').should('be.visible');

    // click "Végétation": it becomes selected
    cy.contains('Végétation').click({ force: true });
    cy.contains('Tout').should('not.have.class', 'selected');
    cy.contains('Écosystèmes').should('not.have.class', 'selected');
    cy.contains('Végétation').should('have.class', 'selected');
    cy.contains('espèces sur les 100').should('not.be.visible');
    cy.contains('de la forêt disparaît').should('be.visible');

    // click "Végétation" again: it becomes not selected
    cy.contains('Végétation').click({ force: true });
    cy.contains('Tout').should('not.have.class', 'selected');
    cy.contains('Écosystèmes').should('not.have.class', 'selected');
    cy.contains('Végétation').should('not.have.class', 'selected');
    cy.contains('espèces sur les 100').should('not.be.visible');
    cy.contains('de la forêt disparaît').should('not.be.visible');

    // click "Végétation" and "Écosystèmes": since all categories are selected, "All" should become selected
    cy.contains('Végétation').click({ force: true });
    cy.contains('Écosystèmes').click({ force: true });
    cy.contains('Tout').should('have.class', 'selected');
    cy.contains('Écosystèmes').should('not.have.class', 'selected');
    cy.contains('Végétation').should('not.have.class', 'selected');
    cy.contains('espèces sur les 100').should('be.visible');
    cy.contains('de la forêt disparaît').should('be.visible');
  });
});
