describe('About', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('À propos').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Pourquoi un compteur de la biodiversité en outre-mer ?');
    cy.get('footer').should('contain', 'Territoires');
  });

  it('should navigate to indicators', () => {
    cy.visit('/');
    cy.contains('À propos').click();
    cy.contains('Voir tous les indicateurs').click();
    cy.title().should('eq', 'Indicateurs');
  });

  it('should navigate to territories', () => {
    cy.visit('/');
    cy.contains('À propos').click();
    cy.contains('Voir les territoires').click();
    cy.title().should('eq', 'Territoires');
  });

  it('should navigate to act together', () => {
    cy.visit('/');
    cy.contains('À propos').click();
    cy.contains('Agir ensemble').trigger('mouseover');
    cy.contains('Découvrir').click({ force: true });
    cy.title().should('eq', 'Agir ensemble');
  });
});
