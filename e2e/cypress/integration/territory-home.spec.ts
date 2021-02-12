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

  it('should display territories in map', () => {
    cy.visit('/territoires');
    cy.contains('.nav-slide', 'La Réunion');
    cy.contains('.nav-slide', 'Saint-Pierre-et-Miquelon').click();
    cy.contains('h4', 'Saint-Pierre-et-Miquelon');

    cy.contains('.active a', 'Découvrir ce portrait').click({ force: true });
    cy.title().should('eq', 'Saint Pierre et Miquelon');
  });

  it('should display zone in world map', () => {
    cy.visit('/territoires');
    cy.get('.bassin0 .bassin-circle').click({ force: true });

    cy.contains('.bassin-text-title', 'Bassin Antillais');
    cy.contains('.cta-close-bassin', 'Fermer').click({ force: true });

    cy.get('map-container').should('not.contain', 'Bassin Antillais');
  });

  it('should display territory in map', () => {
    cy.visit('/territoires');
    cy.get('.hotspot7 .hotspot-circle').click({ force: true });

    cy.contains('h4', 'Saint-Pierre-et-Miquelon');
  });
});
