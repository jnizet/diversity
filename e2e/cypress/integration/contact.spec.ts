describe('Contact', () => {
  beforeEach(() => {
    cy.server();
    cy.route({
      method: 'POST',
      url: '/messages'
    }).as('sendMessage');
  });

  it('should toggle contact', () => {
    cy.visit('/');

    cy.get('#contact-form').should('not.be.visible');
    cy.get('footer').contains('Contact').click();
    cy.get('#contact-form').should('be.visible');
    cy.get('footer').contains('Contact').click();
    cy.get('#contact-form').should('not.be.visible');
  });

  it('should validate contact form', () => {
    cy.visit('/');

    cy.get('footer').contains('Contact').click();

    cy.get('footer').contains('Veuillez saisir une adresse email valide').should('not.be.visible');
    cy.get('footer').contains('Veuillez saisir un message').should('not.be.visible');

    cy.get('#contact-from').type('hello');
    cy.get('footer').contains('Envoyer').click();

    cy.get('footer').contains('Veuillez saisir une adresse email valide').should('be.visible');
    cy.get('footer').contains('Veuillez saisir un message').should('be.visible');

    cy.get('#contact-body').type('Hi there!');
    cy.get('footer').contains('Nombre de caractères restants: 691');
    cy.get('footer').contains('Envoyer').click();

    cy.get('footer').contains('Veuillez saisir un message').should('not.be.visible');

    cy.get('#contact-body').clear();
    cy.get('footer').contains('Nombre de caractères restants: 700');
    cy.get('footer').contains('Envoyer').click();

    cy.get('footer').contains('Veuillez saisir un message').should('be.visible');
  });

  it('should send an email', () => {
    cy.visit('/');

    cy.get('footer').contains('Contact').click();

    cy.get('#contact-from').type('test@mnhn.fr');
    cy.get('#contact-body').type('test message');
    cy.get('#contact-send').click();

    cy.get('#contact-from').should('be.disabled');
    cy.get('#contact-body').should('be.disabled');
    cy.get('#contact-send').should('be.disabled');

    cy.wait('@sendMessage');

    cy.get('#contact-form').should('not.be.visible');

    cy.get('footer').contains('Contact').click();

    cy.get('#contact-from').should('be.enabled');
    cy.get('#contact-body').should('be.enabled');
    cy.get('#contact-send').should('be.enabled');
  });

  it('should show an error if sending an email fails', () => {
    cy.route({
      method: 'POST',
      url: '/messages',
      status: 500,
      response: {}
    }).as('errorSendingMessage');

    cy.visit('/');

    cy.get('footer').contains('Contact').click();

    cy.get('#contact-from').type('test@mnhn.fr');
    cy.get('#contact-body').type('test message');
    cy.get('#contact-send').click();

    cy.get('#contact-from').should('be.disabled');
    cy.get('#contact-body').should('be.disabled');
    cy.get('#contact-send').should('be.disabled');

    cy.wait('@errorSendingMessage')
      .should('have.property', 'status', 500);

    cy.get('#contact-form').should('be.visible');
    cy.get('#contact-error').should('be.visible');
  });
});
