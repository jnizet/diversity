describe('Home', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Découvrez la biodiversité des Outre-Mer');
    cy.get('footer').should('contain', 'Territoires');
  });

  it('should have multi-size images', () => {
    cy.visit('/');

    cy.get('img[src="/images/1/image"]').should(
      'have.attr',
      'srcset',
      '/images/1/image/sm 576w, /images/1/image/md 768w, /images/1/image/lg 1200w, /images/1/image/xl 1900w'
    );
  });
});
