describe('Act together', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Agir ensemble').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Agir ensemble pour une biodiversité unique mais fragile');
    cy.get('footer').should('contain', 'Territoires');
  });

  it('should navigate to ecogesture', () => {
    cy.visit('/agir-ensemble');
    cy.contains('Protégeons les récifs coralliens').click();
    cy.title().should('eq', 'Écogeste: protéger les récifs');
  });

  it('should navigate to ecogestures home', () => {
    cy.visit('/agir-ensemble');
    cy.contains('voir tous les écogestes').click();
    cy.title().should('eq', 'Écogestes');
  });

  // TODO test navigation to science
});
