describe('Ecogesture', () => {
  it('should have a header, title and footer', () => {
    cy.visit('/');
    cy.contains('Éco-gestes').click();
    cy.contains('Protégeons les récifs coralliens').click();

    cy.get('header').should('contain', 'Territoires');
    cy.get('h1').should('contain', 'Protégeons les récifs coralliens');
    cy.get('footer').should('contain', 'Territoires');
  });

  it('should have a presentation', () => {
    cy.visit('/ecogestes/recifs');
    cy.get('p').should('contain', 'Sinon ils vont mourir');
    cy.get('div.ecogeste-thematique').should('contain', 'Loisirs');
    cy.url().then(url => {
      cy.get('.share-link.twitter').should(
        'have.attr',
        'href',
        'https://twitter.com/intent/tweet?text=Prot%C3%A9geons%20les%20r%C3%A9cifs%20coralliens%0A' + url
      );
      cy.get('.share-link.facebook').should(
        'have.attr',
        'href',
        'https://www.facebook.com/share.php?u=' + url + '&quote=Prot%C3%A9geons%20les%20r%C3%A9cifs%20coralliens'
      );
    });
  });

  it('should have a quote', () => {
    cy.visit('/ecogestes/recifs');
    cy.get('blockquote').should('contain', 'Ces actions conjuguées entraînent un accroissement de la biodiversité...');
  });

  it('should have a linked indicator (randomly picked)', () => {
    cy.visit('/ecogestes/recifs');
    cy.get('div.indicateur-number-small').should('contain', '5'); // it's either 5% or 65, as it random
  });

  it('should have actions', () => {
    cy.visit('/ecogestes/recifs');
    cy.get('.section-action').should('contain', "Je choisis une crème solaire non nocive pour l'environnement");
    cy.get('.section-action').should('contain', "En bateau, je ne jette pas l'ancre à proximité de récifs");
  });

  it('should have a section to act for science', () => {
    cy.visit('/ecogestes/recifs');
    cy.get('.section-agir').should('contain', 'Agir pour la science');
    cy.get('.section-agir').should('contain', 'J’agis en ligne');
    cy.get('.section-agir').should('contain', 'Je donne de mon temps');
  });

  it('should have a link to the next ecogesture', () => {
    cy.visit('/ecogestes/recifs');
    cy.get('.section-push-next').should('have.attr', 'href', '/ecogestes/recifs1');
  });
});
