describe('Contact', () => {
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
    cy.get('footer').contains('Nombre de caractères restants : 691');
    cy.get('footer').contains('Envoyer').click();

    cy.get('footer').contains('Veuillez saisir un message').should('not.be.visible');

    cy.get('#contact-body').clear();
    cy.get('footer').contains('Nombre de caractères restants : 700');
    cy.get('footer').contains('Envoyer').click();

    cy.get('footer').contains('Veuillez saisir un message').should('be.visible');
  });

  it('should send an email', () => {
    cy.route2('POST', '/messages', {
      delayMs: 1000
    }).as('sendMessage');
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

  it('should really send an email', () => {
    cy.visit('/');

    cy.get('footer').contains('Contact').click();

    cy.get('#contact-from').type('test@mnhn.fr');
    cy.get('#contact-body').type('test message');
    cy.get('#contact-send').click();

    cy.get('#contact-form').should('not.be.visible');
  });

  it('should show an error if sending an email fails', () => {
    cy.route2('POST', '/messages', {
      statusCode: 500,
      delayMs: 1000,
      body: {}
    }).as('errorSendingMessage');

    cy.visit('/');

    cy.get('footer').contains('Contact').click();

    cy.get('#contact-from').type('test@mnhn.fr');
    cy.get('#contact-body').type('test message');
    cy.get('#contact-send').click();

    cy.get('#contact-from').should('be.disabled');
    cy.get('#contact-body').should('be.disabled');
    cy.get('#contact-send').should('be.disabled');

    cy.wait('@errorSendingMessage');

    cy.get('#contact-form').should('be.visible');
    cy.get('#contact-error').should('be.visible');
  });

  it('should show an error if network fails', () => {
    cy.route2('POST', '/messages', {
      forceNetworkError: true
    });

    cy.visit('/');

    cy.get('footer').contains('Contact').click();

    cy.get('#contact-from').type('test@mnhn.fr');
    cy.get('#contact-body').type('test message');
    cy.get('#contact-send').click();

    cy.get('#contact-form').should('be.visible');
    cy.get('#contact-error').should('be.visible');
  });
});
