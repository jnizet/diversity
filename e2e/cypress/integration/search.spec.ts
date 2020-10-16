describe('Search', () => {
  it('should toggle search', () => {
    cy.visit('/');

    // no search input initially
    cy.get('#search').should('not.be.visible');

    // toggle search, it should be visible. Type some text in it
    cy.get('#open-search-button').click();
    cy.get('#search').should('be.visible').type('test');

    // toggle search, it should become invisible but keep its text
    cy.get('#close-search-button').click();
    cy.get('#search').should('not.be.visible');

    // toggle search, it should become visible
    cy.get('#open-search-button').click();
    cy.get('#search').should('be.visible').should('have.value', 'test');
  });

  it('should search', () => {
    cy.visit('/');

    // toggle search
    cy.get('#open-search-button').click();

    // enter text and then enter
    cy.get('#search').type('compteur biodiversité').type('{enter}');

    // the search page should be displayed
    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Résultats de la recherche');
    cy.get('footer').should('contain', 'Territoires');

    // the search input should be displayed with the text pre-filled
    cy.get('#open-search-button').click();
    cy.get('#search').should('be.visible').should('have.value', 'compteur biodiversité');
    cy.get('#close-search-button').click();

    // click on result "À propos"
    cy.get('main').contains('À propos').click();

    // it should navigate to the linked a propos page
    cy.get('h1').should('contain', 'Pourquoi un compteur de la biodiversité en outre-mer ?');
  });
});
