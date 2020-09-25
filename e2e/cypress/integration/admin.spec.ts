describe('Admin', () => {
  function login() {
    cy.clearLocalStorage('user');
    cy.visit('/admin');

    cy.get('#login').type('admin');
    cy.get('#password').type('password');
    cy.contains('Connexion').click();

    cy.contains('Administration');
  }

  it('should have an admin home page protected by password', () => {
    login();
  });

  describe('after authentication', () => {
    beforeEach(() => {
      login();
    });

    it('should have an indicator categories page', () => {
      cy.visit('/admin/indicator-categories');
      cy.contains('Catégories des indicateurs');
    });
  });
});
