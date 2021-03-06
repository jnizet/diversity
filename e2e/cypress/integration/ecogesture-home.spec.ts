describe('Ecogesture home', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Éco-gestes').click();

    cy.get('header').should('contain', 'Territoires');

    // top
    cy.get('h1').should('contain', 'Réinventons notre façon de vivre et de voyager grâce aux écogestes');

    // intro
    cy.get('.section-ecogeste-intro').should('contain', "Qu'est-ce qu'un écogeste ?");

    // quote
    cy.get('.section-citation').should('contain', 'Des actions concrètes');

    // other ecogestures
    cy.get('.section-push-next').should('have.attr', 'href', 'https://agir.biodiversitetousvivants.fr/les-gestes/');
    cy.get('.section-push-next').should('contain', 'Retrouvez d’autres écogestes sur');

    cy.get('footer').should('contain', 'Territoires');
  });

  it('should show more ecogestures', () => {
    cy.visit('/ecogestes');

    // ecogestures
    // only 6 displayed
    cy.get('.ecogeste-item:visible').should('have.length', 4);
    // but 13 in total
    cy.get('.ecogeste-item').should('have.length', 13);
    cy.get('#see-more').should('be.visible');

    // click on See more
    cy.get('#see-more').click({ force: true });
    // display all
    cy.get('.ecogeste-item:visible').should('have.length', 13);

    cy.get('#see-more').should('not.be.visible');
  });
});
