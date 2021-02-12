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

  it('should display territories in map', () => {
    cy.visit('/');
    cy.contains('.nav-slide', 'La Réunion');
    cy.contains('h4', 'La Réunion');

    cy.contains('.nav-slide', 'Saint-Pierre-et-Miquelon').click();
    cy.contains('h4', 'Saint-Pierre-et-Miquelon');

    cy.contains('.active a', 'Découvrir ce portrait').click({ force: true });
    cy.title().should('eq', 'Saint Pierre et Miquelon');
  });

  it('should display zone in world map', () => {
    cy.visit('/');
    cy.contains('tous les territoires').click({ force: true });
    cy.get('.bassin0 .bassin-circle').click({ force: true });

    cy.contains('.bassin-text-title', 'Bassin Antillais');
    cy.contains('.cta-close-bassin', 'Fermer').click({ force: true });

    cy.get('map-container').should('not.contain', 'Bassin Antillais');
  });

  it('should display territory in map', () => {
    cy.visit('/');
    cy.contains('tous les territoires').click({ force: true });
    cy.get('.hotspot7 .hotspot-circle').click({ force: true });

    cy.contains('h4', 'Saint-Pierre-et-Miquelon');
  });
});
