<?php

namespace App\Http\Middleware;

use App\Models\User;
use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Symfony\Component\HttpFoundation\Response;

/**
 * Authenticates app requests with the X-Mynd-Token header.
 */
class AuthenticateWithToken
{
    public const HEADER = 'X-Mynd-Token';

    public function handle(Request $request, Closure $next): Response
    {
        $token = $request->header(self::HEADER);

        $user = is_string($token) && $token !== ''
            ? User::query()->where('api_token', $token)->first()
            : null;

        if ($user === null) {
            return response()->json(['message' => 'Unauthenticated.'], 401);
        }

        Auth::setUser($user);

        return $next($request);
    }
}
