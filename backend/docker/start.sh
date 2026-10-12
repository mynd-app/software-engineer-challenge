#!/bin/sh
# Container entrypoint: install dependencies on first boot, then serve the API on :8080.
set -e

if [ ! -f vendor/autoload.php ]; then
    composer install --no-interaction
fi

exec php artisan serve --host=0.0.0.0 --port=8080
