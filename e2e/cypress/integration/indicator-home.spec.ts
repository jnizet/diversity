describe('Indicator home', () => {
  it('should have a header, title, indicators and footer', () => {
    cy.visit('/');
    cy.contains('Indicateurs').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Compter la biodiversité, oui mais comment ?');
    cy.get('#categories').should('contain', 'TOUT');
    cy.get('#categories').should('contain', 'Écosystèmes');
    cy.get('#indicators').should('contain', '65');
    cy.get('#indicators').should('contain', 'des espèces considérées');
    cy.get('#onb').should('contain', "L'observatoire national de la biodiversité");
    cy.get('#quote').should('contain', 'Documenter et présenter');
    cy.get('#learn').should('contain', "Qu'est-ce que la biodiversité ?");
    cy.get('footer').should('contain', 'Territoires');
  });

  it('should link to indicator', () => {
    cy.visit('/indicateurs');

    cy.get('.indicateur-number').should('contain', '65');
    cy.get('.indicateur-description').should('contain', 'des espèces considérées');
    cy.get('.indicateur-link').should('have.attr', 'href', '/indicateurs/especes-envahissantes');
  });

  it('should filter indicators by categories', () => {
    cy.visit('/indicateurs');

    // initially, "All" is selected, categories are not deselected, and all indicators are visible
    cy.contains('TOUT').should('have.class', 'active');
    cy.contains('Écosystèmes').should('not.have.class', 'active');
    cy.contains('Végétation').should('not.have.class', 'active');
    cy.contains('des espèces considérées').should('be.visible');
    cy.contains('de la forêt disparaît').should('be.visible');

    // click "Végétation": it becomes selected
    cy.contains('Végétation').click({ force: true });
    cy.contains('TOUT').should('not.have.class', 'active');
    cy.contains('Écosystèmes').should('not.have.class', 'active');
    cy.contains('Végétation').should('have.class', 'active');
    cy.contains('des espèces considérées').should('not.be.visible');
    cy.contains('de la forêt disparaît').should('be.visible');

    // click "Végétation" again: it becomes not selected
    // so "All" is selected again
    cy.contains('Végétation').click({ force: true });
    cy.contains('TOUT').should('have.class', 'active');
    cy.contains('Écosystèmes').should('not.have.class', 'active');
    cy.contains('Végétation').should('not.have.class', 'active');
    cy.contains('des espèces considérées').should('be.visible');
    cy.contains('de la forêt disparaît').should('be.visible');

    // click "Végétation" and "Écosystèmes": since all categories are selected, "All" should become selected
    cy.contains('Végétation').click({ force: true });
    cy.contains('Écosystèmes').click({ force: true });
    cy.contains('TOUT').should('have.class', 'active');
    cy.contains('Écosystèmes').should('not.have.class', 'active');
    cy.contains('Végétation').should('not.have.class', 'active');
    cy.contains('des espèces considérées').should('be.visible');
    cy.contains('de la forêt disparaît').should('be.visible');
  });

  it('should open/close questions', () => {
    cy.visit('/indicateurs');
    cy.contains('La majorité des territoires d’outre-mer').should('not.be.visible');
    cy.contains("Avec ses territoires d'outre-mer").should('not.be.visible');

    // click a question to see the answer and quote
    cy.contains("Qu'est-ce que la biodiversité ?").click({ force: true });
    cy.contains("La majorité des territoires d'outre-mer").should('be.visible');
    cy.contains("Avec ses territoires d'outre-mer").should('be.visible');

    // click on another question to see the answer and quote
    cy.contains('Les outre-mer, des points chauds de biodiversité').click({ force: true });
    cy.contains('Réponse 2').should('be.visible');
    cy.contains('Citation 2').should('be.visible');
    // first one is still visible
    cy.contains("La majorité des territoires d'outre-mer").should('be.visible');
    cy.contains("Avec ses territoires d'outre-mer").should('be.visible');

    // click again on a question to close it
    cy.contains("Qu'est-ce que la biodiversité ?").click({ force: true });
    cy.contains("La majorité des territoires d'outre-mer").should('not.be.visible');
    cy.contains("Avec ses territoires d'outre-mer").should('not.be.visible');
    // second one is still visible
    cy.contains('Réponse 2').should('be.visible');
    cy.contains('Citation 2').should('be.visible');
  });
});
