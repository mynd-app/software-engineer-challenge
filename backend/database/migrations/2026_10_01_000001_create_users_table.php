<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('users', function (Blueprint $table) {
            $table->id();
            $table->foreignId('company_id')->nullable()->constrained()->nullOnDelete();
            $table->string('name');
            $table->string('email')->unique();
            $table->string('team')->nullable();
            $table->string('role')->default('member');
            $table->string('timezone')->default('Australia/Melbourne');
            $table->string('api_token', 80)->unique();

            // NULL inherits the company default; true/false is an explicit override.
            $table->boolean('insights_enabled')->nullable();

            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};
