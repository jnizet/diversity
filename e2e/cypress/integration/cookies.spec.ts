describe('Contact', () => {
  beforeEach(() => {
    cy.clearLocalStorage('cookies-consent');
  });

  it('should display cookies warning, hide it when accepted, and then not display it again', () => {
    cy.visit('/');

    const text = 'Les cookies assurent le bon fonctionnement de nos services';
    cy.contains(text).should('be.visible');
    cy.contains('Accepter et fermer').click();
    cy.contains(text).should('not.be.visible');

    cy.visit('/apropos');
    cy.contains(text).should('not.be.visible');
  });
});
