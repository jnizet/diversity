describe('About', () => {
  it('should have an admin home page', () => {
    cy.visit('/admin');
    cy.contains('Administration');

    cy.visit('/admin/');
    cy.contains('Administration');
  });

  it('should have an indicator categories page', () => {
    cy.visit('/admin/indicator-categories');
    cy.contains('Catégories des indicateurs');
  });
});
