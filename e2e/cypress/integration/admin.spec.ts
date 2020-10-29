/**
 * NOTE: This test is *expected* to fail when run during development, when running `yarn e2e`,
 * because it connects to the backend server running with the e2e profile, on port 8081,
 * where the admin application is not bundled.
 *
 * When running `yarn e2e:standalone` (which must be run after `./gradlew assemble`), or when running on CI,
 * then the tests are executed against the actual jar produced by the build, and embedding the angular application.
 */
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
